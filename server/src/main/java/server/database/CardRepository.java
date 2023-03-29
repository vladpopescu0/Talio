package server.database;

import commons.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CardRepository extends JpaRepository<Card, Long> {
    //@Modifying
    //@Query(value = "DELETE FROM CARD_LIST_CARDS clc WHERE clc.CARDS_ID = ?1")
    //void deleteInRelationByCardId(long id);

    /**
     * Returns the Cards associated with a Tag of given ID
     * @param id ID of the Tag
     * @return the list of Cards associated with a Tag of given ID
     */
    List<Card> findByTags_Id(long id);
}
