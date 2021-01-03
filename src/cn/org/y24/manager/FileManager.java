package cn.org.y24.manager;

import cn.org.y24.actions.FileAction;
import cn.org.y24.actions.HuffmanAction;
import cn.org.y24.entity.FileEntity;
import cn.org.y24.entity.HuffmanEntity;
import cn.org.y24.enums.FileActionType;
import cn.org.y24.enums.HuffmanActionType;
import cn.org.y24.interfaces.IManager;
import cn.org.y24.interfaces.IUrlProvider;
import cn.org.y24.utils.*;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

// read regular files that are to be tared from local or cloud
// write tared file to os or cloud
public class FileManager implements IManager<FileAction>, IUrlProvider {
    FileActionType actionType;

    @Override
    public boolean execute(FileAction fileAction) {
        final var entity = (FileEntity) fileAction.getEntity();
        actionType = (FileActionType) fileAction.getType();
        switch (actionType) {
            case readLocalUnTarFile -> {
                final String rootDir = entity.getLocation();
                final List<String> fileNames = new LinkedList<>();
                final CharCountTable countTable = CharCountTable.ofInstance();
                File rootFile = new File(rootDir);
                if (rootFile.exists()) {
                    boolean result = rootFile.isDirectory() ?
                            ExternalInFileWorker.processAllFiles(rootFile, fileNames, countTable)
                            : ExternalInFileWorker.processSingleFile(rootFile, fileNames, countTable);
                    // +1 means rootDir/file
                    final var relativeFileNames = fileNames.stream().map(s -> s.substring(rootDir.length() + 1)).collect(Collectors.toList());
                    fileAction.setEntity(new FileEntity(rootDir) {{
                        this.setHeader(NewTarHeader.defaultHeader("y24"));
                        this.setFileNames(relativeFileNames);
                        this.setCharCountTable(countTable);
                    }});
                    return result;
                } else
                    return false;
            }
            case readLocalTarFile -> {
                NewTarInFileWorker inWorker = NewTarInFileWorker.ofInstance(entity.getLocation(), entity.getCredential());
                if (inWorker.isNull())
                    return false;
                final var header = inWorker.getHeader();
                final var charEncodeTable = inWorker.getCharCodeTable();
                final var fileNameList = inWorker.getFileNameList();
                final var fileBlocksCount = inWorker.getFileBlockCount();
                final var fileBlocks = inWorker.getFileBlocks(fileBlocksCount);
                if (header.isNull()
                        || fileNameList.isNull()
                        || charEncodeTable.isNull()
                        || fileBlocksCount < 0
                        || fileBlocks == null)
                    return false;
                fileAction.setEntity(new FileEntity(entity.getLocation()) {{
                    this.setHeader(header);
                    this.setFileNameList(fileNameList);
                    this.setCharCodeTable(charEncodeTable);
                    this.setFileBodyBlocks(fileBlocks);
                }});
                return true;
            }
            case writeLocalTarFile -> {
                NewTarOutFileWorker outWorker = NewTarOutFileWorker.ofInstance(entity.getDestination(),
                        entity.getAlgorithm(),
                        entity.getCredential());
                if (outWorker.isNull())
                    return false;
                final var headerStatus = outWorker.writeHeader(entity.getHeader());
                final var charEncodeTableStatus = outWorker.writeCharCode(entity.getCharCodeTable());
                final var result = ExternalOutFileWorker.getAllFileBlockInfo(entity.getLocation(), entity.getFileNames(), entity.getCharCodeTable());
                if (result.isEmpty())
                    return false;
                final FileNameList fileNameList = FileNameList.ofInstance();
                final List<FileBodyBlock> fileBodyBlockList = new LinkedList<>();
                result.forEach(fileBlockInfo -> {
                    fileNameList.addAll(fileBlockInfo.getFileName(), fileBlockInfo.getBlockIds());
                    fileBlockInfo.getBlocks().forEach(block -> fileBodyBlockList.add(new FileBodyBlock(block.getId()) {{
                        this.setCheckSum(block.getCheckSum());
                        this.setPadding(block.getPadding());
                        this.setVersion(block.getVersion());
                        this.setContent(block.getContent());
                    }}));
                });
                final var fileNameListStatus = outWorker.writeFileNameList(fileNameList);
                final var fileBlocksStatus = outWorker.writeFileBlocks(fileBodyBlockList);
                outWorker.close();
                return headerStatus && charEncodeTableStatus && fileNameListStatus && fileBlocksStatus;
            }
            case writeLocalUnTarFile -> {
                final var rootDir = entity.getDestination() + (entity.getDestination().endsWith(File.separator) ? "" : File.separator);
                File rootDirFile = new File(rootDir);
                if (!rootDirFile.mkdir())
                    return false;
                final var charCodeTable = entity.getCharCodeTable();
                final FileNameList fileNameList = entity.getFileNameList();
                final var fileBodyBlocks = entity.getFileBodyBlocks();
                final Map<Integer, FileBodyBlock> pool = new HashMap<>(fileBodyBlocks.size());
                fileBodyBlocks.forEach(block -> pool.put(block.getId(), block));
                final BitByteFactory factory = BitByteFactory.ofInstance();
                final HuffmanManager huffmanManager = new HuffmanManager();
                /// Note: Huffman Tree restore here.
                final HuffmanEntity huffmanEntity = new HuffmanEntity(charCodeTable);
                final var entries = fileNameList.allEntries();
                for (var entry : entries) {
                    final List<char[]> data = new LinkedList<>();
                    final List<FileBodyBlock> blocks = entry.getValue().stream().map(pool::get).collect(Collectors.toList());
                    for (var block : blocks) {
                        factory.acceptBytes(DataConverter.bytesToList(block.getContent()), block.getPadding());
                        final var bits = factory.result();
                        huffmanEntity.setInput(DataConverter.listToBytes(bits));
                        HuffmanAction huffmanAction = new HuffmanAction(HuffmanActionType.defaultDecode, huffmanEntity);
                        if (!huffmanManager.execute(huffmanAction))
                            return false;
                        final HuffmanEntity huffmanActionEntity = (HuffmanEntity) huffmanAction.getEntity();
                        data.add(huffmanActionEntity.getOutput());
                    }
                    if (!ExternalOutFileWorker.writeFile(rootDir, entry.getKey(), data))
                        return false;
                }
            }
            case readRemoteTarFile -> {
                final var url = entity.getLocation();
                var destinationDir = entity.getDestination();
                // URL: ntfp://y24.org.cn:2424/username/path-to-file
                // URL: ntfp://localhost:2424/username/path-to-file
                // URL: ntfp://127.0.0.1:2424/username/path-to-file
                UrlHandler handler = null;
                try {
                    if (!new File(destinationDir).exists())
                        return false;
                    destinationDir = destinationDir + (destinationDir.endsWith(File.separator) ? "" : File.separator);
                    final var paths = url.split(File.separator);
                    final File outFile = new File(destinationDir + paths[paths.length - 1]);
                    if (outFile.exists()) {
                        if (!outFile.delete())
                            return false;
                    }
                    final var outStream = new BufferedOutputStream(new FileOutputStream(outFile));
                    handler = new UrlHandler();
                    if (!handler.handle(getUrl(), new HashMap<>() {{
                        this.put("location", entity.getLocation());
                    }})) {
                        handler.dispose();
                        return false;
                    }
                    final var reader = handler.getReader();
                    if (!reader.readLine().startsWith("OK")) {
                        handler.dispose();
                        return false;
                    }
                    char[] buffer;
                    int size;
                    do {
                        size = reader.read();
                        if (size == 0)
                            break;
                        buffer = new char[size];
                        if (reader.read(buffer) < size)
                            return false;
                        System.out.println("current: " + buffer.length);
                        outStream.write(Base64.getDecoder().decode(String.valueOf(buffer)));
                    } while (true);
                    reader.close();
                    outStream.flush();
                    outStream.close();
                    handler.dispose();
                    return true;

                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    if (handler != null) handler.dispose();
                }
            }
            case writeRemoteTarFile -> {
                final var paths = entity.getLocation().split(File.separator);
                final String fileName = paths[paths.length - 1];
                var destinationDir = entity.getDestination();
                destinationDir = destinationDir + (destinationDir.endsWith(File.separator) ? "" : File.separator);
                final File inFile = new File(entity.getLocation());
                if (!entity.getLocation().endsWith(NewTarFileSpec.suffix) || !inFile.exists() || inFile.isDirectory())
                    return false;
                UrlHandler handler = null;
                try {
                    final var inReader = new BufferedInputStream(new FileInputStream(inFile));
                    final Map<String, String> content = new LinkedHashMap<>();
                    byte[] buffer = new byte[NewTarFileSpec.maxBlockLength];
                    int count;
                    int i = 0;
                    do {
                        count = inReader.read(buffer);
                        if (count == -1)
                            break;
                        final var current = Arrays.copyOf(buffer, count);
                        content.put("content" + i++, URLEncoder.encode(Base64.getUrlEncoder().encodeToString(current), StandardCharsets.UTF_8));
                    } while (true);
                    final Map<String, String> options = new LinkedHashMap<>();
                    options.put("destination", destinationDir + fileName);
                    options.put("count", "" + content.size());
                    options.putAll(content);
                    inReader.close();
                    handler = new UrlHandler();
                    if (!handler.handle(getUrl(), options)) {
                        handler.dispose();
                        return false;
                    }
                    final var reader = handler.getReader();
                    if (!reader.readLine().startsWith("OK")) {
                        handler.dispose();
                        return false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    if (handler != null) handler.dispose();
                }
            }
        }
        return true;
    }


    @Override
    public String getUrl() {
        return baseUrl + actionType.toString();
    }
}
