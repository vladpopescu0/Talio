package server.api;

import commons.Tag;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestTagRepository implements TagRepository {

    public final List<Tag> tags = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    public void call(String name) { calledMethods.add(name); }

    /**
     * @return All tags found in the repository
     */
    @Override
    public List<Tag> findAll() {
        call("findAll");
        return tags;
    }

    /**
     * @param sort Sort option for queries
     * @return All tags found in the repository under given Sort
     */
    @Override
    public List<Tag> findAll(Sort sort) {
        return this.findAll(sort);
    }

    /**
     * @param pageable pagination information
     * @return All tags found in the repository as paginated information
     */
    @Override
    public Page<Tag> findAll(Pageable pageable) {
        return this.findAll(pageable);
    }

    /**
     * @param longs must not be {@literal null} nor contain any {@literal null} values.
     * @return All tags found in the repository with matching ids longs
     */
    @Override
    public List<Tag> findAllById(Iterable<Long> longs) {
        return this.findAllById(longs);
    }

    /**
     * @return the number of records in the repository
     */
    @Override
    public long count() {
        return this.count();
    }

    /**
     * @param aLong must not be {@literal null}.
     */
    @Override
    public void deleteById(Long aLong) {
        call("deleteById");
        tags.remove(this.getById(aLong));
    }

    /**
     * @param entity must not be {@literal null}.
     */
    @Override
    public void delete(Tag entity) {

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
    public void deleteAll(Iterable<? extends Tag> entities) {

    }

    /**
     *
     */
    @Override
    public void deleteAll() {

    }

    /**
     * @param entity must not be {@literal null}.
     * @param <S> subclass of Tag
     * @return the entity to be saved
     */
    @Override
    public <S extends Tag> S save(S entity) {
        call("save");
        entity.setId((long) tags.size());
        tags.add(entity);
        return entity;
    }

    /**
     * @param entities must not be {@literal null} nor must it contain {@literal null}.
     * @param <S> subclass of Tag
     * @return the entities to be saved
     */
    @Override
    public <S extends Tag> List<S> saveAll(Iterable<S> entities) {
        return this.saveAll(entities);
    }

    /**
     * @param aLong must not be {@literal null}.
     * @return Optional of Tag with given ID
     */
    @Override
    public Optional<Tag> findById(Long aLong) {
        call("findById");
        return find(aLong);
    }

    private Optional<Tag> find(long id) {
        return tags.stream()
                .filter(u -> u.getId() == id).findFirst();
    }

    /**
     * @param aLong must not be {@literal null}.
     * @return whether a record with the given ID exists in the repository
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
     * @param <S> subclass of Tag
     * @return the entity to be saved
     */
    @Override
    public <S extends Tag> S saveAndFlush(S entity) {
        return entity;
    }

    /**
     * @param entities entities to be saved. Must not be {@literal null}.
     * @param <S> subclass of Tag
     * @return entities to be saved
     */
    @Override
    public <S extends Tag> List<S> saveAllAndFlush(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        entities.forEach(result::add);
        return result;
    }

    /**
     * @param entities entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllInBatch(Iterable<Tag> entities) {

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
     * @return record from the repository with the given ID
     */
    @Override
    public Tag getOne(Long aLong) {
        return this.getOne(aLong);
    }

    /**
     * @param aLong must not be {@literal null}.
     * @return a record from the repository with a matching ID
     */
    @Override
    public Tag getById(Long aLong) {
        call("getById");
        return find(aLong).get();
    }

    /**
     * @param example must not be {@literal null}.
     * @param <S> subclass of Tag
     * @return an entity from the repository that matches example
     */
    @Override
    public <S extends Tag> Optional<S> findOne(Example<S> example) {
        return this.findOne(example);
    }

    /**
     * @param example must not be {@literal null}.
     * @param <S> subclass of Tag
     * @return all entities from the repository that match entries example
     */
    @Override
    public <S extends Tag> List<S> findAll(Example<S> example) {
        return this.findAll(example);
    }

    /**
     * @param example must not be {@literal null}.
     * @param sort    the {@link Sort} specification to sort
     *               the results by, must not be {@literal null}.
     * @param <S> subclass of Tag
     * @return all entities from the repository that match entries from example under given Sort
     */
    @Override
    public <S extends Tag> List<S> findAll(Example<S> example, Sort sort) {
        return this.findAll(example, sort);
    }

    /**
     * @param example  must not be {@literal null}.
     * @param pageable can be {@literal null}.
     * @param <S> subclass of Tag
     * @return all entities from the repository
     * that match entries from example as paginated information
     */
    @Override
    public <S extends Tag> Page<S> findAll(Example<S> example, Pageable pageable) {
        return this.findAll(example, pageable);
    }

    /**
     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
     * @param <S> subclass of Tag
     * @return number of entities that match entries from example
     */
    @Override
    public <S extends Tag> long count(Example<S> example) {
        return this.count(example);
    }

    /**
     * @param example the {@link Example} to use for the
     *                existence check. Must not be {@literal null}.
     * @param <S> subclass of Tag
     * @return whether example exists in the repository
     */
    @Override
    public <S extends Tag> boolean exists(Example<S> example) {
        return this.exists(example);
    }

    /**
     * @param example       must not be {@literal null}.
     * @param queryFunction the query function defining projection, sorting, and the result type
     * @param <S> subclass of Tag
     * @param <R> generic type
     * @return result of the given query
     */
    @Override
    public <S extends Tag, R> R findBy(Example<S> example,
                                        Function<FluentQuery
                                                .FetchableFluentQuery<S>, R>
                                                queryFunction) {
        return this.findBy(example, queryFunction);
    }
}