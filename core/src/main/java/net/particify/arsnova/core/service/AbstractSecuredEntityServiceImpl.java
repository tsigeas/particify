package net.particify.arsnova.core.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

import net.particify.arsnova.core.model.Deletion.Initiator;
import net.particify.arsnova.core.model.Entity;

public abstract class AbstractSecuredEntityServiceImpl<E extends Entity> implements EntityService<E> {
  private final Class<E> type;
  private final EntityService<E> entityService;

  public AbstractSecuredEntityServiceImpl(final Class<E> type, final EntityService<E> entityService) {
    this.type = type;
    this.entityService = entityService;
  }

  @Override
  @PreAuthorize("hasPermission(#id, #this.this.getTypeName(), 'read')")
  public E get(final String id) {
    return entityService.get(id);
  }

  @Override
  @PreAuthorize("denyAll")
  public E get(final String id, final boolean internal) {
    return null;
  }

  @Override
  @PreFilter(value = "hasPermission(filterObject, #this.this.getTypeName(), 'read')", filterTarget = "ids")
  public List<E> get(final Iterable<String> ids) {
    return entityService.get(ids);
  }

  @Override
  @PreAuthorize("hasPermission(#entity, 'create')")
  public E create(final E entity) {
    return entityService.create(entity);
  }

  @Override
  @PreFilter(value = "hasPermission(filterObject, 'create')", filterTarget = "entities")
  public List<E> create(final List<E> entities) {
    return entityService.create(entities);
  }

  @Override
  @PreAuthorize("hasPermission(#entity, 'update')")
  public E update(final E entity) {
    return entityService.update(entity);
  }

  @Override
  @PreAuthorize("hasPermission(#entity, 'update')")
  public E update(final E entity, final Class<?> view) {
    return entityService.update(entity, view);
  }

  @Override
  @PreAuthorize("hasPermission(#oldEntity, 'update')")
  public E update(final E oldEntity, final E newEntity, final Class<?> view) {
    return entityService.update(oldEntity, newEntity, view);
  }

  @Override
  @PreAuthorize("hasPermission(#entity, 'update')")
  public E patch(final E entity, final Map<String, Object> changes) throws IOException {
    return entityService.patch(entity, changes);
  }

  @Override
  @PreAuthorize("hasPermission(#entity, 'update')")
  public E patch(final E entity, final Map<String, Object> changes, final Class<?> view) throws IOException {
    return entityService.patch(entity, changes, view);
  }

  @Override
  @PreAuthorize("hasPermission(#entity, 'update')")
  public E patch(
      final E entity,
      final Map<String, Object> changes,
      final Function<E, ?> propertyGetter) throws IOException {
    return entityService.patch(entity, changes, propertyGetter);
  }

  @Override
  @PreAuthorize("hasPermission(#entity, 'update')")
  public E patch(
      final E entity,
      final Map<String, Object> changes,
      final Function<E, ?> propertyGetter,
      final Class<?> view) throws IOException {
    return entityService.patch(entity, changes, propertyGetter, view);
  }

  @Override
  @PreFilter(value = "hasPermission(filterObject, 'update')", filterTarget = "entities")
  public List<E> patch(final Iterable<E> entities, final Map<String, Object> changes) throws IOException {
    return entityService.patch(entities, changes);
  }

  @Override
  @PreFilter(value = "hasPermission(filterObject, 'update')", filterTarget = "entities")
  public List<E> patch(
      final Iterable<E> entities,
      final Map<String, Object> changes,
      final Class<?> view) throws IOException {
    return entityService.patch(entities, changes, view);
  }

  @Override
  @PreFilter(value = "hasPermission(filterObject, 'update')", filterTarget = "entities")
  public List<E> patch(
      final Iterable<E> entities,
      final Map<String, Object> changes,
      final Function<E, ?> propertyGetter) throws IOException {
    return entityService.patch(entities, changes, propertyGetter);
  }

  @Override
  @PreFilter(value = "hasPermission(filterObject, 'update')", filterTarget = "entities")
  public List<E> patch(
      final Iterable<E> entities,
      final Map<String, Object> changes,
      final Function<E, ?> propertyGetter, final Class<?> view) throws IOException {
    return entityService.patch(entities, changes, propertyGetter, view);
  }

  @Override
  @PreAuthorize("hasPermission(#entity, 'delete')")
  public void delete(final E entity) {
    entityService.delete(entity);
  }

  @Override
  @PreFilter(value = "hasPermission(filterObject, 'delete')", filterTarget = "entities")
  public void delete(final Iterable<E> entities, final Initiator initiator) {
    entityService.delete(entities, initiator);
  }

  public String getTypeName() {
    return type.getSimpleName().toLowerCase();
  }
}
