package general;

public interface CrudInterface<Entity, Key> {
    Entity create(Entity entity);

    boolean remove(Key key);
}
