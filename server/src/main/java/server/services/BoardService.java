package server.services;

import commons.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;

@Service
public class BoardService extends GenericService<Board> {

    @Autowired
    public BoardService(BoardRepository repo) {super(repo);}

    @Override
    public Board getById(long id) {
        return super.getById(id);
    }

    /**
     * @param board board to be added
     * @return null if the Board is null or has a null field, else the board
     */
    public Board add(Board board) {
        if (board == null) {
            return null;
        }
        return repo.save(board);
    }

    /**
     * Changes the name of a given board
     * @param target the board whose name is to change
     * @param name the name that will substitute the old name
     * @return true if successful, false otherwise
     */
    public boolean changeName(Board target, String name) {
        if (target != null && this.repo.existsById(target.getId())) {
            target.setName(name);
            return true;
        }
        return false;
    }
}
