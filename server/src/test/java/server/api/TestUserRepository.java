package server.api;

import commons.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestUserRepository implements UserRepository {

    public final List<User> users = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) { calledMethods.add(name); }
    /**
     * @param username the name in search
     * @return
     */
    @Override
    public Optional<List<User>> findByUsernameIs(String username) {
        List<User> search = new ArrayList<>();
        for (User u: users) {
            if (u.getUsername().equals(username)) {
                search.add(u);
            }
        }
        call("findByUsernameIs");
        return Optional.of(search);
    }

    /**
     * @param name the name of the user in search
     * @return
     */
    @Override
    public boolean existsByUsername(String name) {
        call("existsByUsername");
        return findByUsernameIs(name).isPresent() &&
                findByUsernameIs(name).get().size() > 0;
    }

    /**
     * @return
     */
    @Override
    public List<User> findAll() {
        call("findAll");
        return users;
    }

    /**
     * @param sort
     * @return
     */
    @Override
    public List<User> findAll(Sort sort) {
        return null;
    }

    /**
     * @param pageable
     * @return
     */
    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }

    /**
     * @param longs must not be {@literal null} nor contain any {@literal null} values.
     * @return
     */
    @Override
    public List<User> findAllById(Iterable<Long> longs) {
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
        users.remove(this.getById(aLong));
    }

    /**
     * @param entity must not be {@literal null}.
     */
    @Override
    public void delete(User entity) {

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
    public void deleteAll(Iterable<? extends User> entities) {

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
    public <S extends User> S save(S entity) {
        call("save");
        entity.setId((long) users.size());
        users.add(entity);
        return entity;
    }

    /**
     * @param entities must not be {@literal null} nor must it contain {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /**
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public Optional<User> findById(Long aLong) {
        call("findById");
        return find(aLong);
    }

    private Optional<User> find(long id) {
        return users.stream()
                .filter(u -> u.getId() == id).findFirst();
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
    public <S extends User> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * @param entities entities to be saved. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    /**
     * @param entities entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllInBatch(Iterable<User> entities) {

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
    public User getOne(Long aLong) {
        return null;
    }

    /**
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public User getById(Long aLong) {
        call("getById");
        return find(aLong).get();
    }

    /**
     * @param example must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * @param example must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> List<S> findAll(Example<S> example) {
        return null;
    }

    /**
     * @param example must not be {@literal null}.
     * @param sort    the {@link Sort} specification to sort
     *               the results by, must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    /**
     * @param example  must not be {@literal null}.
     * @param pageable can be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> long count(Example<S> example) {
        return 0;
    }

    /**
     * @param example the {@link Example} to use for the
     *                existence check. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> boolean exists(Example<S> example) {
        return false;
    }

    /**
     * @param example       must not be {@literal null}.
     * @param queryFunction the query function defining projection, sorting, and the result type
     * @param <S>
     * @param <R>
     * @return
     */
    @Override
    public <S extends User, R> R findBy(Example<S> example,
                                        Function<FluentQuery
                                                .FetchableFluentQuery<S>, R>
                                                queryFunction) {
        return null;
    }
}
