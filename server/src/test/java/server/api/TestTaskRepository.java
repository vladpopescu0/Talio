package server.api;

import commons.Task;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestTaskRepository implements TaskRepository {

    public final List<Task> tasks = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    /**
     * Adds a method to calledMethods
     * @param name the method
     */
    private void call(String name) { calledMethods.add(name); }
    /**
     * @return
     */
    @Override
    public List<Task> findAll() {
        call("findAll");
        return tasks;
    }

    /**
     * @param sort
     * @return
     */
    @Override
    public List<Task> findAll(Sort sort) {
        return null;
    }

    /**
     * @param pageable
     * @return
     */
    @Override
    public Page<Task> findAll(Pageable pageable) {
        return null;
    }

    /**
     * @param longs must not be {@literal null} nor contain any {@literal null} values.
     * @return
     */
    @Override
    public List<Task> findAllById(Iterable<Long> longs) {
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
        tasks.remove(this.getById(aLong));
    }

    /**
     * @param entity must not be {@literal null}.
     */
    @Override
    public void delete(Task entity) {

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
    public void deleteAll(Iterable<? extends Task> entities) {

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
    public <S extends Task> S save(S entity) {
        call("save");
        Optional<Task> opt = find(entity.getId());
        if (opt.isEmpty()) {
            entity.setId((long) tasks.size());
            tasks.add(entity);
        } else {
            int ind = tasks.indexOf(opt.get());
            tasks.remove(opt.get());
            entity.setId(ind);
            tasks.add(ind, entity);
        }
        return entity;
    }

    /**
     * @param entities must not be {@literal null} nor must it contain {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Task> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /**
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public Optional<Task> findById(Long aLong) {
        call("findById");
        return find(aLong);
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
    public <S extends Task> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * @param entities entities to be saved. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Task> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    /**
     * @param entities entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllInBatch(Iterable<Task> entities) {

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
    public Task getOne(Long aLong) {
        return null;
    }

    /**
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public Task getById(Long aLong) {
        call("getById");
        return find(aLong).get();
    }

    /**
     * finds a task
     * @param id the id
     * @return  Optional containing the task
     */
    private Optional<Task> find(Long id) {
        return tasks.stream()
                .filter(t -> t.getId() == id).findFirst();
    }


    /**
     * @param example must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Task> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * @param example must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Task> List<S> findAll(Example<S> example) {
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
    public <S extends Task> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    /**
     * @param example  must not be {@literal null}.
     * @param pageable can be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Task> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Task> long count(Example<S> example) {
        return 0;
    }

    /**
     * @param example the {@link Example} to use for the existence check.
     *               Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Task> boolean exists(Example<S> example) {
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
    public <S extends Task, R> R findBy(Example<S> example,
                                        Function<FluentQuery.
                                                FetchableFluentQuery<S>, R>
                                                queryFunction) {
        return null;
    }
}
