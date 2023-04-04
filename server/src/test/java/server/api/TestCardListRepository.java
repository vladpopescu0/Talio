package server.api;

import commons.CardList;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.CardListRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TestCardListRepository implements CardListRepository {

    public final List<CardList> lists = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    /**
     * Calls a method
     * @param name the name of the method
     */
    private void call(String name) { calledMethods.add(name); }

    /**
     * @return all the cardLists
     */
    @Override
    public List<CardList> findAll() {
        call("findAll");
        return lists;
    }

    /**
     * @param sort AUTO-GENERATED
     * @return AUTO-GENERATED
     */
    @Override
    public List<CardList> findAll(Sort sort) {
        return null;
    }

    /**
     * @param pageable AUTO-GENERATED
     * @return AUTO-GENERATED
     */
    @Override
    public Page<CardList> findAll(Pageable pageable) {
        return null;
    }

    /**
     * @param longs must not be {@literal null} nor contain any {@literal null} values.
     * @return AUTO-GENERATED
     */
    @Override
    public List<CardList> findAllById(Iterable<Long> longs) {
        return null;
    }

    /**
     * @return AUTO-GENERATED
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
        lists.remove(this.getById(aLong));
    }

    /**
     * @param entity must not be {@literal null}.
     */
    @Override
    public void delete(CardList entity) {

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
    public void deleteAll(Iterable<? extends CardList> entities) {

    }

    /**
     *AUTO-GENERATED
     */
    @Override
    public void deleteAll() {

    }

    /**
     * @param entity must not be {@literal null}.
     * @param <S> AUTO-GENERATED
     * @return AUTO-GENERATED
     */
    @Override
    public <S extends CardList> S save(S entity) {
        call("save");
        Optional<CardList> opt = find(entity.getId());
        if (opt.isEmpty()) {
            entity.setId((long) lists.size());
            lists.add(entity);
        } else {
            int ind = lists.indexOf(opt.get());
            lists.remove(opt.get());
            entity.setId((long) ind);
            lists.add(ind, entity);
        }
        return entity;
    }

    /**
     * @param entities must not be {@literal null} nor must it contain {@literal null}.
     * @param <S> AUTO-GENERATED
     * @return AUTO-GENERATED
     */
    @Override
    public <S extends CardList> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /**
     * @param aLong must not be {@literal null}.
     * @return AUTO-GENERATED
     */
    @Override
    public Optional<CardList> findById(Long aLong) {
        call("findById");
        return find(aLong);
    }

    /**
     * Find a CardList
     * @param id the id of it
     * @return Optional with the CardList
     */
    private Optional<CardList> find (long id) {
        return lists.stream()
                .filter(b -> b.getId() == id).findFirst();
    }

    /**
     * @param aLong must not be {@literal null}.
     * @return true if the CardList is in the repo
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
     * @param <S> AUTO-GENERATED
     * @return AUTO-GENERATED
     */
    @Override
    public <S extends CardList> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * @param entities entities to be saved. Must not be {@literal null}.
     * @param <S> AUTO-GENERATED
     * @return AUTO-GENERATED
     */
    @Override
    public <S extends CardList> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    /**
     * @param entities entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllInBatch(Iterable<CardList> entities) {

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
     * @return AUTO-GENERATED
     */
    @Override
    public CardList getOne(Long aLong) {
        return null;
    }

    /**
     * @param aLong must not be {@literal null}.
     * @return AUTO-GENERATED
     */
    @Override
    public CardList getById(Long aLong) {
        call("getById");
        return find(aLong).get();
    }

    /**
     * @param example must not be {@literal null}.
     * @param <S> AUTO-GENERATED
     * @return AUTO-GENERATED
     */
    @Override
    public <S extends CardList> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * @param example must not be {@literal null}.
     * @param <S> AUTO-GENERATED
     * @return AUTO-GENERATED
     */
    @Override
    public <S extends CardList> List<S> findAll(Example<S> example) {
        return null;
    }

    /**
     * @param example must not be {@literal null}.
     * @param sort    the {@link Sort} specification to sort
     *               the results by, must not be {@literal null}.
     * @param <S> AUTO-GENERATED
     * @return AUTO-GENERATED
     */
    @Override
    public <S extends CardList> List<S> findAll(Example<S> example,
                                                Sort sort) {
        return null;
    }

    /**
     * @param example  must not be {@literal null}.
     * @param pageable can be {@literal null}.
     * @param <S> AUTO-GENERATED
     * @return AUTO-GENERATED
     */
    @Override
    public <S extends CardList> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
     * @param <S> AUTO-GENERATED
     * @return AUTO-GENERATED
     */
    @Override
    public <S extends CardList> long count(Example<S> example) {
        return 0;
    }

    /**
     * @param example the {@link Example} to use for
     *                the existence check. Must not be {@literal null}.
     * @param <S> AUTO-GENERATED
     * @return AUTO-GENERATED
     */
    @Override
    public <S extends CardList> boolean exists(Example<S> example) {
        return false;
    }

    /**
     * @param example       must not be {@literal null}.
     * @param queryFunction the query function defining projection, sorting, and the result type
     * @param <S> AUTO-GENERATED
     * @param <R> AUTO-GENERATED
     * @return AUTO-GENERATED
     */
    @Override
    public <S extends CardList, R> R findBy(Example<S> example,
                                            Function<FluentQuery
                                                    .FetchableFluentQuery<S>, R>
                                                    queryFunction) {
        return null;
    }

    /**
     * Finds all Cards which have a Tag of given ID attached to them
     * @param id ID of the Tag
     * @return a list of Cards with Tag of given ID attached to them
     */
    @Override
    public CardList findByCards_Id(long id) {
        call("findByCards_Id");
        return findByCardId(id).get(0);
    }

    /**
     * Helper method for findByTags_Id finding all Cards
     * which have a Tag of given ID attached to them
     * @param id ID of the Tag
     * @return a list of Cards with Tag of given ID attached to them
     */
    private List<CardList> findByCardId(long id) {
        return lists.stream().filter(l -> l.getCards().stream().anyMatch(c -> c.getId() == id))
                .collect(Collectors.toList());
    }

    /**
     * Checks whether there exists a CardList that is a parent of the Card of the given ID
     * @param id ID of the Card
     * @return whether there exists a CardList that is a parent of the Card of the given ID
     */
    @Override
    public boolean existsByCards_Id(long id) {
        call("existsByCards_Id");
        return findByCardId(id).size() > 0;
    }
}
