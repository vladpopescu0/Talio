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

import client.scenes.*;
import client.scenes.BoardsOverviewCtrl;
import client.scenes.CreateBoardViewCtrl;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

import client.scenes.MainCtrl;

public class MyModule implements Module {

    /**
     * Configures the controllers for the scenes
     * @param binder binds the controllers
     */
    @Override
    public void configure(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(BoardsOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(BoardViewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddCardCtrl.class).in(Scopes.SINGLETON);
        binder.bind(CreateListCtrl.class).in(Scopes.SINGLETON);
        binder.bind(CreateBoardViewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(UserCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ChangeNameCtrl.class).in(Scopes.SINGLETON);
        binder.bind(EditBoardNameViewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(CustomizationPageCtrl.class).in(Scopes.SINGLETON);
        binder.bind(EditCardCtrl.class).in(Scopes.SINGLETON);
        binder.bind(JoinBoardByLinkCtrl.class).in(Scopes.SINGLETON);
        binder.bind(CardDetailsViewCtr.class).in(Scopes.SINGLETON);
    }
}