package server.api;

import commons.Board;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TestBoardRepository implements BoardRepository {

    public final List<Board> boards = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) { calledMethods.add(name); }
    /**
     * @param id the id of the user
     * @return
     */
    @Override
    public List<Board> findByUsers_Id(long id) {
        call("findByUsers_Id");
        return findByUser(id);
    }

    /**
     * @param id the id of the user
     * @return
     */
    @Override
    public boolean existsByUsers_Id(long id) {
        call("existsByUsers_Id");
        return findByUser(id).size() > 0;
    }

    private List<Board> findByUser(long id) {
        return boards.stream().filter(b -> b.hasUser(id))
                .collect(Collectors.toList());
    }

    /**
     * @return
     */
    @Override
    public List<Board> findAll() {
        call("findAll");
        return boards;
    }

    /**
     * @param sort
     * @return
     */
    @Override
    public List<Board> findAll(Sort sort) {
        return null;
    }

    /**
     * @param pageable
     * @return
     */
    @Override
    public Page<Board> findAll(Pageable pageable) {
        return null;
    }

    /**
     * @param longs must not be {@literal null} nor contain any {@literal null} values.
     * @return
     */
    @Override
    public List<Board> findAllById(Iterable<Long> longs) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public long count() {
        return 0;
    }

    /**
     * @param aLong must not be {@literal null}.
     */
    @Override
    public void deleteById(Long aLong) {
        call("deleteById");
        boards.remove(this.getById(aLong));
    }

    /**
     * @param entity must not be {@literal null}.
     */
    @Override
    public void delete(Board entity) {

    }

    /**
     * @param longs must not be {@literal null}. Must not contain {@literal null} elements.
     */
    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    /**
     * @param entities must not be {@literal null}. Must not contain {@literal null} elements.
     */
    @Override
    public void deleteAll(Iterable<? extends Board> entities) {

    }

    /**
     *
     */
    @Override
    public void deleteAll() {

    }

    /**
     * @param entity must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Board> S save(S entity) {
        call("save");
        Optional<Board> opt = find(entity.getId());
        if (opt.isEmpty()) {
            entity.setId((long) boards.size());
            boards.add(entity);
        } else {
            int ind = boards.indexOf(opt.get());
            boards.remove(opt.get());
            entity.setId((long) ind);
            boards.add(ind, entity);
        }
        return entity;
    }

    /**
     * @param entities must not be {@literal null} nor must it contain {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Board> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /**
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public Optional<Board> findById(Long aLong) {
        call("findById");
        return find(aLong);
    }

    private Optional<Board> find(Long id) {
        return boards.stream()
            .filter(b -> b.getId() == id).findFirst();
    }

    /**
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public boolean existsById(Long aLong) {
        call("existsById");
        return find(aLong).isPresent();
    }

    /**
     *
     */
    @Override
    public void flush() {

    }

    /**
     * @param entity entity to be saved. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Board> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * @param entities entities to be saved. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Board> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    /**
     * @param entities entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllInBatch(Iterable<Board> entities) {

    }

    /**
     * @param longs the ids of the entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    /**
     *
     */
    @Override
    public void deleteAllInBatch() {

    }

    /**
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public Board getOne(Long aLong) {
        return null;
    }

    /**
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public Board getById(Long aLong) {
        call("getById");
        return find(aLong).get();
    }

    /**
     * @param example must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Board> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * @param example must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Board> List<S> findAll(Example<S> example) {
        return null;
    }

    /**
     * @param example must not be {@literal null}.
     * @param sort    the {@link Sort} specification to sort the results by,
     *               must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Board> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    /**
     * @param example  must not be {@literal null}.
     * @param pageable can be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Board> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Board> long count(Example<S> example) {
        return 0;
    }

    /**
     * @param example the {@link Example} to use for the existence check.
     *                Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Board> boolean exists(Example<S> example) {
        return false;
    }

    /**
     * @param example       must not be {@literal null}.
     * @param queryFunction the query function defining projection,
     *                     sorting, and the result type
     * @param <S>
     * @param <R>
     * @return
     */
    @Override
    public <S extends Board, R> R findBy(Example<S> example,
                                         Function<FluentQuery
                                                 .FetchableFluentQuery<S>, R>
                                                 queryFunction) {
        return null;
    }
}
