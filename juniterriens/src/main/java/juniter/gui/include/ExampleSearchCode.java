package juniter.gui.include;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.util.stream.Collectors;

public class ExampleSearchCode {


    public class Person {

        private SimpleIntegerProperty id;
        private SimpleStringProperty name;
        private SimpleBooleanProperty isEmployed;

        public Person(Integer id, String name, boolean isEmployed) {
            this.id = new SimpleIntegerProperty(id);
            this.name = new SimpleStringProperty(name);
            this.isEmployed = new SimpleBooleanProperty(isEmployed);
        }

        public int getId() {
            return id.get();
        }

        public IntegerProperty idProperty() {
            return id;
        }

        public void setId(int id) {
            this.id.set(id);
        }

        public String getName() {
            return name.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public boolean getIsEmployed() {
            return isEmployed.get();
        }

        public BooleanProperty isEmployedProperty() {
            return isEmployed;
        }

        public void setIsEmployed(boolean isEmployed) {
            this.isEmployed.set(isEmployed);
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", isEmployed=" + isEmployed +
                    '}';
        }


    }

    public static final int PAGE_ITEMS_COUNT = 10;

    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private Pagination pagination;
    @FXML
    private Label searchLabel;

    private ObservableList<Person> masterData = FXCollections.observableArrayList();

    public ExampleSearchCode() {
        masterData.add(new Person(5, "John", true));
        masterData.add(new Person(7, "Albert", true));
        masterData.add(new Person(11, "Monica", false));
    }

    @FXML
    private void initialize() {

        // search panel
        searchButton.setText("Search");
        searchButton.setOnAction(event -> loadData());
        searchButton.setStyle("-fx-background-color: #457ecd; -fx-text-fill: #ffffff;");

        searchField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                loadData();
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchLabel.setText(newValue);
        });

        pagination.setPageFactory(ExampleSearchCode.this::createPage);
    }

    private Node createPage(Integer pageIndex) {

        VBox dataContainer = new VBox();

        TableView<Person> tableView = new TableView<>(masterData);
        TableColumn id = new TableColumn("ID");
        TableColumn name = new TableColumn("NAME");
        TableColumn employed = new TableColumn("EMPLOYED");

        tableView.getColumns().addAll(id, name, employed);
        dataContainer.getChildren().add(tableView);

        return dataContainer;
    }

    private void loadData() {

        String searchText = searchField.getText();

        Task<ObservableList<Person>> task = new Task<ObservableList<Person>>() {
            @Override
            protected ObservableList<Person> call() {
                updateMessage("Loading data");
                return FXCollections.observableArrayList(masterData
                        .stream()
                        .filter(value -> value.getName().toLowerCase().contains(searchText))
                        .collect(Collectors.toList()));
            }
        };

        task.setOnSucceeded(event -> {
            masterData = task.getValue();
            pagination.setVisible(true);
            pagination.setPageCount(masterData.size() / PAGE_ITEMS_COUNT);
        });

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

}