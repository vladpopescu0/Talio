package server.api;

import commons.Card;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.CardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TestCardRepository implements CardRepository {

    public final List<Card> cards = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    /**
     * Adds a method to calledMethods
     * @param name the method
     */
    private void call(String name) { calledMethods.add(name); }

    /**
     * returns all cards
     * @return the cards
     */
    @Override
    public List<Card> findAll() {
        call("findAll");
        return cards;
    }

    /**
     * AUTO-GENERATED
     * @param sort AUTO-GENERATED
     * @return AUTO-GENERATED
     */
    @Override
    public List<Card> findAll(Sort sort) {
        return null;
    }

    /**
     * AUTO-GENERATED
     * @param pageable AUTO-GENERATED
     * @return AUTO-GENERATED
     */
    @Override
    public Page<Card> findAll(Pageable pageable) {
        return null;
    }

    /**
     * AUTO-GENERATED
     * @param longs must not be {@literal null} nor contain any {@literal null} values.
     * @return AUTO-GENERATED
     */
    @Override
    public List<Card> findAllById(Iterable<Long> longs) {
        return null;
    }

    /**
     * AUTO-GENERATED
     * @return AUTO-GENERATED
     */
    @Override
    public long count() {
        return cards.size();
    }

    /**
     * deleted a card by ID
     * @param aLong must not be {@literal null}.
     */
    @Override
    public void deleteById(Long aLong) {
        call("deleteById");
        cards.remove(this.getById(aLong));
    }

    /**
     * AUTO-GENERATED
     * @param entity must not be {@literal null}.
     */
    @Override
    public void delete(Card entity) {

    }

    /**
     * AUTO-GENERATED
     * @param longs must not be {@literal null}. Must not contain {@literal null} elements.
     */
    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    /**
     * AUTO-GENERATED
     * @param entities must not be {@literal null}. Must not contain {@literal null} elements.
     */
    @Override
    public void deleteAll(Iterable<? extends Card> entities) {

    }

    /**
     * AUTO-GENERATED
     */
    @Override
    public void deleteAll() {

    }

    /**
     * Saves a card
     * @param entity must not be {@literal null}.
     * @return the card
     * @param <S> class
     */
    @Override
    public <S extends Card> S save(S entity) {
        call("save");
        Optional<Card> opt = find(entity.getId());
        if (opt.isEmpty()) {
            entity.setId((long) cards.size());
            cards.add(entity);
        } else {
            int ind = cards.indexOf(opt.get());
            cards.remove(opt.get());
            entity.setId(ind);
            cards.add(ind, entity);
        }
        return entity;
    }

    /**
     * AUTO-GENERATED
     * @param entities must not be {@literal null} nor must it contain {@literal null}.
     * @return AUTO-GENERATED
     * @param <S> AUTO-GENERATED
     */
    @Override
    public <S extends Card> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /**
     * Finds by ID
     * @param aLong must not be {@literal null}.
     * @return the card
     */
    @Override
    public Optional<Card> findById(Long aLong) {
        call("findById");
        return find(aLong);
    }

    /**
     * @param aLong must not be {@literal null}.
     * @return true if the card exists
     */
    @Override
    public boolean existsById(Long aLong) {
        call("existsById");
        return find(aLong).isPresent();
    }

    /**
     * AUTO-GENERATED
     */
    @Override
    public void flush() {

    }

    /**
     * AUTO-GENERATED
     * @param entity entity to be saved. Must not be {@literal null}.
     * @return AUTO-GENERATED
     * @param <S> AUTO-GENERATED
     */
    @Override
    public <S extends Card> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * AUTO-GENERATED
     * @param entities entities to be saved. Must not be {@literal null}.
     * @return AUTO-GENERATED
     * @param <S> AUTO-GENERATED
     */
    @Override
    public <S extends Card> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    /**
     * AUTO-GENERATED
     * @param entities entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllInBatch(Iterable<Card> entities) {

    }

    /**
     * AUTO-GENERATED
     * @param longs the ids of the entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    /**
     * AUTO-GENERATED
     */
    @Override
    public void deleteAllInBatch() {

    }

    /**
     * AUTO-GENERATED
     * @param aLong must not be {@literal null}.
     * @return AUTO-GENERATED
     */
    @Override
    public Card getOne(Long aLong) {
        return null;
    }

    /**
     * get by id
     * @param aLong must not be {@literal null}.
     * @return the card
     */
    @Override
    public Card getById(Long aLong) {
        call("getById");
        return find(aLong).get();
    }

    /**
     * finds a card
     * @param id the id
     * @return  Optional containing the card
     */
    private Optional<Card> find(Long id) {
        return cards.stream()
            .filter(q -> q.getId() == id).findFirst();
    }

    /**
     * AUTO-GENERATED
     * @param example must not be {@literal null}.
     * @return AUTO-GENERATED
     * @param <S> AUTO-GENERATED
     */
    @Override
    public <S extends Card> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * AUTO-GENERATED
     * @param example must not be {@literal null}.
     * @return AUTO-GENERATED
     * @param <S> AUTO-GENERATED
     */
    @Override
    public <S extends Card> List<S> findAll(Example<S> example) {
        return null;
    }

    /**
     * AUTO-GENERATED
     * @param example must not be {@literal null}.
     * @param sort the {@link Sort} specification
     *            to sort the results by, must not be {@literal null}.
     * @return AUTO-GENERATED
     * @param <S> AUTO-GENERATED
     */
    @Override
    public <S extends Card> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    /**
     * AUTO-GENERATED
     * @param example must not be {@literal null}.
     * @param pageable can be {@literal null}.
     * @return AUTO-GENERATED
     * @param <S> AUTO-GENERATED
     */
    @Override
    public <S extends Card> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * AUTO-GENERATED
     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
     * @return AUTO-GENERATED
     * @param <S> AUTO-GENERATED
     */
    @Override
    public <S extends Card> long count(Example<S> example) {
        return 0;
    }

    /**
     * AUTO-GENERATED
     * @param example the {@link Example}
     *                to use for the existence check. Must not be {@literal null}.
     * @return AUTO-GENERATED
     * @param <S> AUTO-GENERATED
     */
    @Override
    public <S extends Card> boolean exists(Example<S> example) {
        return false;
    }

    /**
     * AUTO-GENERATED
     * @param example must not be {@literal null}.
     * @param queryFunction the query function defining projection, sorting, and the result type
     * @return AUTO-GENERATED
     * @param <S> AUTO-GENERATED
     * @param <R> AUTO-GENERATED
     */
    @Override
    public <S extends Card, R> R findBy(Example<S> example,
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
    public List<Card> findByTags_Id(long id) {
        call("findByTags_Id");
        return findByTagId(id);
    }

    /**
     * Helper method for findByTags_Id finding all Cards
     * which have a Tag of given ID attached to them
     * @param id ID of the Tag
     * @return a list of Cards with Tag of given ID attached to them
     */
    private List<Card> findByTagId(long id) {
        return cards.stream().filter(c -> c.getTags().stream().anyMatch(t -> t.getId() == id))
                .collect(Collectors.toList());
    }
}
