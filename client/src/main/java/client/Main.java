/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import static com.google.inject.Guice.createInjector;

import client.scenes.*;
import com.google.inject.Injector;

import client.scenes.BoardViewCtrl;
import client.scenes.BoardsOverviewCtrl;
import client.scenes.CreateBoardViewCtrl;
import client.scenes.MainCtrl;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    /**
     * Main method for client
     *
     * @param args an array of Strings used as runtime arguments
     */

    public static void main(String[] args) {
        launch();
    }

    /**
     * The method that sets up the scenes and scene controllers of the client
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     */
    @Override
    public void start(Stage primaryStage) {

        var overview = FXML.load(BoardsOverviewCtrl.class,
                "client", "scenes", "MainPage.fxml");
        var boardView = FXML.load(BoardViewCtrl.class,
                "client", "scenes", "BoardView.fxml");
        var createList = FXML.load(CreateListCtrl.class,
                "client", "scenes", "CreateList.fxml");
        var createBoardCtrl = FXML.load(CreateBoardViewCtrl.class,
                "client", "scenes", "CreateBoard.fxml");
        var changeListNameCtrl = FXML.load(ChangeNameCtrl.class,
                "client", "scenes", "ChangeListName.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);

        primaryStage.setResizable(false); //Force non-resizable view in order to unify UI design
        mainCtrl.initialize(primaryStage, overview, boardView,
                createList, createBoardCtrl, changeListNameCtrl);
    }
}