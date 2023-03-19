package server.database;

import commons.Board;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    /**
     * Returns a list of all boards of a user
     * @param id the id of the user
     * @return all the boards corresponding
     * to the user as a list
     */
    List<Board> findByUsers_Id(long id);

    /**
     * Checks whether a user has joined any boards
     * @param id the id of the user
     * @return true if there are any boards joined by the user,
     * false otherwise
     */
    boolean existsByUsers_Id(long id);
}
