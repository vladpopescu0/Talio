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
package server.database;

import commons.CardList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardListRepository extends JpaRepository<CardList, Long> {
    /**
     * Returns the CardList by its child Card's ID
     * @param id ID of the child Card
     * @return CardList containing Card of given ID
     */
    CardList findByCards_Id(long id);

    /**
     * Checks whether there exists a parent CardList for Card of given ID
     * @param id ID of the Card
     * @return whether there exists a parent CardList for Card of given ID
     */
    boolean existsByCards_Id(long id);
}