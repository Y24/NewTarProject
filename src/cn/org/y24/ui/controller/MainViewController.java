package cn.org.y24.ui.controller;

import cn.org.y24.Main;
import cn.org.y24.actions.AccountAction;
import cn.org.y24.actions.FileAction;
import cn.org.y24.entity.AccountEntity;
import cn.org.y24.entity.FileEntity;
import cn.org.y24.entity.TarFileInfoEntity;
import cn.org.y24.entity.TarMessageEntity;
import cn.org.y24.enums.AccountActionType;
import cn.org.y24.enums.FileActionType;
import cn.org.y24.interfaces.IManager;
import cn.org.y24.manager.AccountManager;
import cn.org.y24.manager.FileManager;
import cn.org.y24.ui.framework.BaseStageController;
import cn.org.y24.ui.framework.Deliverer;
import cn.org.y24.ui.framework.SceneManager;
import cn.org.y24.ui.framework.StageManager;
import cn.org.y24.utils.AboutMessage;
import cn.org.y24.utils.AlertPageUtil;
import cn.org.y24.utils.NewTarFileSpec;
import com.sun.javafx.binding.StringFormatter;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.junit.platform.engine.support.descriptor.FileSystemSource;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainViewController extends BaseStageController implements Initializable {

    private StageManager stageManager;
    private StringProperty cwdStringProperty;
    private IManager<AccountAction> accountManager;
    private AccountEntity account;
    ObservableList<String> fileList;
    @FXML
    private ListView<String> fileListId;
    @FXML
    private TextField cwdId;
    @FXML
    private MenuBar menuBarId;

    @Override
    public void setStageManager(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @Override
    public void receiveMessage() {
        account = (AccountEntity) ((Deliverer) stageManager.receiveBroadcastMessage().toArray()[0]).getMessage();
    }

    private static class CustomCell extends ListCell<String> {
        private final StageManager stageManager;
        private final StringProperty cwd;

        public CustomCell(StageManager stageManager, StringProperty cwd) {
            this.stageManager = stageManager;
            this.cwd = cwd;
        }

        private String getPrefix() {
            String prefix = cwd.getValue();
            return prefix + (prefix.endsWith(File.separator) ? "" : File.separator);
        }

        @Override
        protected void updateItem(String s, boolean b) {
            super.updateItem(s, b);
            final MenuItem tarMenuItem = new MenuItem("tar");
            tarMenuItem.setOnAction(actionEvent -> {
                System.out.println("tar" + actionEvent.getEventType());
                showTarPage(getPrefix() + s);
            });
            final MenuItem unTarMenuItem = new MenuItem("unTar");
            unTarMenuItem.setOnAction(actionEvent -> {
                System.out.println("unTar" + actionEvent.getEventType());
                showUnTarPage(getPrefix() + s);
            });
            final MenuItem infoMenuItem = new MenuItem("info");
            infoMenuItem.setOnAction(actionEvent -> {
                System.out.println("info" + actionEvent.getEventType());
                showInfoPage(getPrefix() + s);
            });
            final ContextMenu fileContextMenu = new ContextMenu(unTarMenuItem, infoMenuItem);
            final ContextMenu dirContextMenu = new ContextMenu(tarMenuItem);
            fileContextMenu.setStyle("-fx-font-size: 18px");
            dirContextMenu.setStyle("-fx-font-size: 18px");
            final File file = new File(getPrefix() + s);
            if (file.exists()) {
                setContextMenu(!file.isDirectory() && s.endsWith(NewTarFileSpec.suffix) ? fileContextMenu : dirContextMenu);
            }
            final Label label = new Label(s);
            label.setOnMouseClicked(mouseEvent -> {
                if (file.exists() && file.isDirectory()) {
                    cwd.setValue(file.getPath());
                }
            });
            setGraphic(label);
        }

        private void showInfoPage(String target) {
            System.out.println(target);
            final TarFileInfoEntity entity = new TarFileInfoEntity();
            FileManager manager = new FileManager();
            FileAction action = new FileAction(FileActionType.readLocalTarFile, new FileEntity(target));
            if (!manager.execute(action)) {
                System.out.println(" Get fileInfo failed!");
                entity.name = target;
                entity.encryption = "Encrypted";
                entity.fileBlocks = "Unknown";
                entity.fileCount = "Unknown";
                entity.integrity = "Unknown";

            } else {
                final var fileEntity = (FileEntity) action.getEntity();
                entity.name = target;
                entity.encryption = "NoEncrypted";
                entity.fileBlocks = "" + fileEntity.getFileBodyBlocks().size();
                entity.fileCount = "" + fileEntity.getFileNameList().size();
                entity.integrity = "Certified";
            }
            SceneManager sceneManager = stageManager.get(Main.primarySceneManagerName);
            stageManager.sendSingleCastMessage(sceneManager.getCurrentScene().hashCode(), 1, entity);
            Parent fileInfoParent = sceneManager.init("FileInfoView.fxml", stageManager);
            if (fileInfoParent == null) {
                System.out.println("fileInfoParent null!");
                return;
            }
            Scene fileInfoScene = new Scene(fileInfoParent, 1000, 600);
            sceneManager.delete("info");
            sceneManager.add(fileInfoScene, "info");
            if (sceneManager.select("info")) {
                stageManager.showAdditional(Main.primarySceneManagerName);
            }

        }

        private void showUnTarPage(String target) {
            System.out.println(target);
            SceneManager sceneManager = stageManager.get(Main.primarySceneManagerName);
            stageManager.sendSingleCastMessage(sceneManager.getCurrentScene().hashCode(), 3, target);
            Parent unTarPageParent = sceneManager.init("UnTarPageView.fxml", stageManager);
            if (unTarPageParent == null) {
                System.out.println("UnTarPageParent null!");
                return;
            }
            Scene tarPageScene = new Scene(unTarPageParent, 1000, 600);
            sceneManager.delete("unTar");
            sceneManager.add(tarPageScene, "unTar");
            if (sceneManager.select("unTar")) {
                stageManager.showAdditional(Main.primarySceneManagerName);
            }
        }

        private void showTarPage(String target) {
            System.out.println(target);
            SceneManager sceneManager = stageManager.get(Main.primarySceneManagerName);
            stageManager.sendSingleCastMessage(sceneManager.getCurrentScene().hashCode(), 2, target);
            Parent tarPageParent = sceneManager.init("TarPageView.fxml", stageManager);
            if (tarPageParent == null) {
                System.out.println("TarPageParent null!");
                return;
            }
            Scene tarPageScene = new Scene(tarPageParent, 1000, 600);
            sceneManager.delete("tar");
            sceneManager.add(tarPageScene, "tar");
            if (sceneManager.select("tar")) {
                stageManager.showAdditional(Main.primarySceneManagerName);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuBarId.setStyle("-fx-font-size: 18px");
        accountManager = new AccountManager();
        cwdStringProperty = new SimpleStringProperty();
        cwdStringProperty.bindBidirectional(cwdId.textProperty());
        fileList = FXCollections.observableArrayList();
        fileListId.setItems(fileList);
        fileListId.setStyle("-fx-font-size: 24px");
        fileListId.setCellFactory(stringListView -> new CustomCell(stageManager, cwdStringProperty));
        cwdStringProperty.addListener((observableValue, s, t1) -> {
            final String location = observableValue.getValue();
            final File file = new File(location);
            fileList.clear();
            if (!file.exists() || file.isFile()) {
                return;
            }
            final var files = file.listFiles();
            if (files == null) {
                return;
            }
            fileList.addAll(Arrays.stream(files).map(File::getName).collect(Collectors.toList()));
        });
    }

    @FXML
    private void doLogout() {
        accountManager.execute(new AccountAction(AccountActionType.logout, account));
        stageManager.convertTo(Main.primarySceneManagerName);
    }

    @FXML
    private void doDispose() {
        accountManager.execute(new AccountAction(AccountActionType.dispose, account));
        stageManager.convertTo(Main.primarySceneManagerName);
    }

    @FXML
    private void showHistory() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("历史记录");
        ListView<String> historyListView = new ListView<>();
        final List<String> content = new ArrayList<>();
//        content.addAll(localSide.parallelStream().map(QueryHistoryEntity::toString).collect(Collectors.toList()));
//        content.addAll(cloudSide.parallelStream().map(QueryHistoryEntity::toString).collect(Collectors.toList()));
        historyListView.setItems(FXCollections.observableArrayList(content));
        historyListView.setFixedCellSize(50);
        historyListView.setStyle("-fx-font-size: 18px");
        historyListView.setPrefWidth(1000);
        alert.setGraphic(historyListView);
        alert.setHeaderText("查询历史");
        alert.showAndWait();
    }

    @FXML
    private void doPull() {
        /*cloudSide.clear();
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setHeaderText("结果");
        alert.setTitle("结果");
        final var action = new HistoryAction(HistoryActionType.fetch, account);
        if (historyManager.execute(action)) {
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText("成功从云端获取数据!");
            cloudSide = action.getHistoryList();
        } else {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("拉取失败!");
        }
        alert.showAndWait();*/
    }

    @FXML
    private void doPush() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setHeaderText("结果");
        alert.setTitle("结果");
     /*   if (localSide.isEmpty()) {
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText("无需同步!");
        } else {
            if (historyManager.execute(new HistoryAction(HistoryActionType.push, account, localSide))) {
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setContentText("成功同步数据到云端!");
            } else {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText("同步失败!");
            }
        }
        localSide.clear();
        alert.showAndWait();*/
    }

    @FXML
    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("关于");
        alert.setHeaderText("相关信息");
        alert.setContentText(new AboutMessage("Y24", "1.0.0", "https://github.com/y24/weather_report", "Linux JavaSE 15/Intellij IDEA Ultimate/OpenJFX 15", "NewTar").toString());
        alert.showAndWait();
    }

    @FXML
    private void showAutoBackupConf(ActionEvent actionEvent) {
    }

/*    @FXML
    private void showAllPlaces() {
        final var action = new CityAction(CityActionType.fetch);
        cityManager.execute(action);
        cityList.clear();
        cityList.addAll(action.getCityList());
        cityNameLabelId.visibleProperty().bind(queryResult);
        cityNameLabelId.textProperty().bind(cityName);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("所有地点");
        ListView<String> cityListView = new ListView<>();
        final List<String> content = cityList.parallelStream().map(cityEntity -> cityEntity.getChinese() + "   ->   " + cityEntity.toString()).collect(Collectors.toList());
        cityListView.setItems(FXCollections.observableArrayList(content));
        cityListView.setFixedCellSize(50);
        cityListView.setStyle("-fx-font-size: 18px");
        cityListView.setPrefWidth(1000);
        alert.setGraphic(cityListView);
        alert.setHeaderText("清单");
        alert.showAndWait();

    }*/
}
