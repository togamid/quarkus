package io.quarkus.arc;

import io.quarkus.arc.impl.Qualifiers;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * Quarkus representation of an injectable bean.
 * This interface extends the standard CDI {@link Bean} interface.
 *
 * @author Martin Kouba
 *
 * @param <T>
 */
public interface InjectableBean<T> extends Bean<T>, InjectableReferenceProvider<T> {

    /**
     * The identifier is generated by the container and is unique for a specific deployment.
     *
     * @return the identifier for this bean
     */
    String getIdentifier();

    /**
     * 
     * @return the kind of the bean
     * @see Kind
     */
    default Kind getKind() {
        return Kind.CLASS;
    }

    /**
     *
     * @return the scope
     */
    @Override
    default Class<? extends Annotation> getScope() {
        return Dependent.class;
    }

    /**
     *
     * @return the set of bean types
     */
    @Override
    Set<Type> getTypes();

    /**
     *
     * @return the set of qualifiers
     */
    @Override
    default Set<Annotation> getQualifiers() {
        return Qualifiers.DEFAULT_QUALIFIERS;
    }

    @Override
    default void destroy(T instance, CreationalContext<T> creationalContext) {
        creationalContext.release();
    }

    /**
     *
     * @return the declaring bean if the bean is a producer method/field, or {@code null}
     */
    default InjectableBean<?> getDeclaringBean() {
        return null;
    }

    @Override
    default String getName() {
        return null;
    }

    @Override
    default Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    @Override
    default Set<InjectionPoint> getInjectionPoints() {
        return Collections.emptySet();
    }

    @Override
    default boolean isNullable() {
        return false;
    }

    @Override
    default boolean isAlternative() {
        return getAlternativePriority() != null;
    }

    /**
     *
     * @return the priority if the bean is an alternative, or {@code null}
     */
    default Integer getAlternativePriority() {
        return null;
    }

    /**
     * @return whether or not the bean is a default bean
     */
    default boolean isDefaultBean() {
        return false;
    }

    /**
     * Suppressed beans cannot be obtained by programmatic lookup via {@link Instance}.
     * 
     * @return {@code true} if the bean should be suppressed
     */
    default boolean isSuppressed() {
        return false;
    }

    enum Kind {

        CLASS,
        PRODUCER_FIELD,
        PRODUCER_METHOD,
        SYNTHETIC,
        INTERCEPTOR,
        DECORATOR;

        public static Kind from(String value) {
            for (Kind kind : values()) {
                if (kind.toString().equals(value)) {
                    return kind;
                }
            }
            return null;
        }

    }

}
