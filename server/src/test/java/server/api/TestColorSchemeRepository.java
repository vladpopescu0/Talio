package server.api;

import commons.ColorScheme;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ColorSchemeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class TestColorSchemeRepository implements ColorSchemeRepository {

    public final List<ColorScheme> colorSchemes = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) { calledMethods.add(name); }
    /**
     * find all mock
     * @return all
     */
    @Override
    public List<ColorScheme> findAll() {
        return null;
    }
    /**
     * find all mock sorted
     * @param sort property
     * @return all sorted
     */
    @Override
    public List<ColorScheme> findAll(Sort sort) {
        return null;
    }
    /**
     * find all mock pageable
     * @param pageable property
     * @return all pageable
     */
    @Override
    public Page<ColorScheme> findAll(Pageable pageable) {
        return null;
    }
    /**
     * find all with id
     * @param longs ids list
     * @return all id
     */
    @Override
    public List<ColorScheme> findAllById(Iterable<Long> longs) {
        return null;
    }
    /**
     * count database function
     */
    @Override
    public long count() {
        return 0;
    }
    /**
     * delete all by id
     */
    @Override
    public void deleteById(Long aLong) {

    }
    /**
     * delete all color schemes like
     * @param entity what to delete
     */
    @Override
    public void delete(ColorScheme entity) {

    }
    /**
     * delete all by id iterables
     */
    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }
    /**
     * delete all by colorSchemes iterables
     */
    @Override
    public void deleteAll(Iterable<? extends ColorScheme> entities) {

    }

    /**
     * exterminate everything
     */
    @Override
    public void deleteAll() {

    }

    /**
     * saves an entity
     * @param entity must not be {@literal null}.
     * @return the entity saved
     * @param <S> entity
     */
    @Override
    public <S extends ColorScheme> S save(S entity) {
        call("save");
        Optional<ColorScheme> opt = find(entity.getId());
        if (opt.isEmpty()) {
            entity.setId((long) colorSchemes.size());
            colorSchemes.add(entity);
        } else {
            int ind = colorSchemes.indexOf(opt.get());
            colorSchemes.remove(opt.get());
            entity.setId(ind);
            colorSchemes.add(ind, entity);
        }
        return entity;
    }

    /**
     * save all like
     * @param entities must not be {@literal null} nor must it contain {@literal null}.
     * @return the list saved
     * @param <S> entities
     */
    @Override
    public <S extends ColorScheme> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /**
     * find by id method
     * @param aLong must not be {@literal null}.
     * @return what is found or nothing
     */
    @Override
    public Optional<ColorScheme> findById(Long aLong) {
        return Optional.empty();
    }

    /**
     * does this exist in the repository
     * @param aLong must not be {@literal null}.
     * @return true if found false otherwise
     */
    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    /**
     * flushed the repository
     */
    @Override
    public void flush() {

    }

    /**
     * methods save and flush at the same time
     * @param entity entity to be saved. Must not be {@literal null}.
     * @return saved identity
     * @param <S>
     */
    @Override
    public <S extends ColorScheme> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * methods saveAll and flush at the same time
     * @param entities entities to be saved. Must not be {@literal null}.
     * @return saved identities
     * @param <S>
     */
    @Override
    public <S extends ColorScheme> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    /**
     * delete all given entities
     * @param entities entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllInBatch(Iterable<ColorScheme> entities) {

    }

    /**
     * delete all by id
     * @param longs the ids of the entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    /**
     * exterminate all
     */
    @Override
    public void deleteAllInBatch() {

    }

    /**
     * get first element by id
     * @param aLong must not be {@literal null}.
     * @return found element
     */
    @Override
    public ColorScheme getOne(Long aLong) {
        return null;
    }

    /**
     * get from database by id
     * @param aLong must not be {@literal null}.
     * @return the found element
     */
    @Override
    public ColorScheme getById(Long aLong) {
        return null;
    }

    /**
     * find one from repo
     * @param example must not be {@literal null}.
     * @return the found element
     * @param <S>
     */
    @Override
    public <S extends ColorScheme> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * find all like
     * @param example must not be {@literal null}.
     * @return query result of find all
     * @param <S>
     */
    @Override
    public <S extends ColorScheme> List<S> findAll(Example<S> example) {
        return null;
    }

    /**
     * find all like, sort parametrized
     * @param example must not be {@literal null}.
     * @param sort the {@link Sort}
     * specification to sort the results by, must not be {@literal null}.
     * @return all found and sorted
     * @param <S>
     */
    @Override
    public <S extends ColorScheme> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    /**
     * finds all like and pages them
     * @param example must not be {@literal null}.
     * @param pageable can be {@literal null}.
     * @return the query output
     * @param <S>
     */
    @Override
    public <S extends ColorScheme> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * how many like @param are
     * @param example the {@link Example} to count instances
     * for. Must not be {@literal null}.
     * @return how many there are in the database
     * @param <S>
     */
    @Override
    public <S extends ColorScheme> long count(Example<S> example) {
        return 0;
    }

    /**
     * exist like @param
     * @param example the {@link Example} to use
     * for the existence check. Must not be {@literal null}.
     * @return true if there is something, false otherwise
     * @param <S>
     */
    @Override
    public <S extends ColorScheme> boolean exists(Example<S> example) {
        return false;
    }

    /**
     * find by field
     * @param example must not be {@literal null}.
     * @param queryFunction the query function defining projection, sorting, and the result type
     * @return true if found
     * @param <S>
     * @param <R>
     */
    @Override
    public <S extends ColorScheme, R> R findBy(Example<S> example
            , Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    /**
     * Universal method find by id
     * @param id the id of the colorscheme
     * @return an optional object on whether it was found
     * or not
     */
    private Optional<ColorScheme> find(Long id) {
        return colorSchemes.stream()
                .filter(c -> Objects.equals(c.getId(), id)).findFirst();
    }
}
