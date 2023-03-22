package server.database;

import commons.Card;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CardRepository extends JpaRepository<Card, Long> {
    //@Modifying
    //@Query(value = "DELETE FROM CARD_LIST_CARDS clc WHERE clc.CARDS_ID = ?1")
    //void deleteInRelationByCardId(long id);

}
