package com.shinybunny.cmdapi.arguments;

import com.shinybunny.cmdapi.CommandAPI;
import com.shinybunny.cmdapi.CommandContext;
import com.shinybunny.cmdapi.CommandManager;
import com.shinybunny.cmdapi.annotations.AnnotationAdapter;
import com.shinybunny.cmdapi.exceptions.IncompatibleAnnotationException;
import com.shinybunny.cmdapi.exceptions.InvalidArgumentException;
import com.shinybunny.cmdapi.exceptions.NoAdapterFoundException;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterArgument extends Argument {

    private Map<Annotation, AnnotationAdapter> annotations;
    private List<Class<? extends Annotation>[]> incompatibleAnnotations = new ArrayList<>();

    public ParameterArgument(CommandManager manager, String name, Annotation[] annotations, Class<?> type, ArgumentAdapter<?> argumentAdapter) throws NoAdapterFoundException, IncompatibleAnnotationException {
        super(manager, name, type, argumentAdapter);
        this.annotations = new HashMap<>();
        this.addAnnotationAdapters(annotations);
    }

    private void addAnnotationAdapters(Annotation[] annotations) throws IncompatibleAnnotationException, NoAdapterFoundException {
        for (Annotation a : annotations) {
            AnnotationAdapter adapter = manager.getAnnotationAdapter(a.annotationType());
            if (adapter != null) {
                // there is an adapter for that annotation!
                this.annotations.put(a, adapter);
                if (!adapter.isRequired(a)) {
                    // mark this argument as not required only if one annotation adapter says so.
                    this.required = false;
                }
                if (!adapter.isSyntax()) {
                    this.syntax = false;
                }
            } else {
                throw new NoAdapterFoundException("No adapter found for annotation " + a.annotationType(),a.annotationType());
            }
        }
        for (Map.Entry<Annotation,AnnotationAdapter> e : this.annotations.entrySet()) {
            // call the init() method of the adapter, to modify this argument according to the annotation.
            e.getValue().init(this, e.getKey());
        }

        // incompatibility check
        for (Class<? extends Annotation>[] incompatibles : incompatibleAnnotations) {
            Class<? extends Annotation> other = null;
            for (Class<? extends Annotation> a : incompatibles) {
                if (hasAnnotation(a)) {
                    if (other == null) {
                        other = a;
                    } else {
                        throw new IncompatibleAnnotationException("Annotation " + a + " is incompatible with " + other,a,other,this);
                    }
                }
            }
        }
    }

    public <A extends Annotation> AnnotationAdapter<A> getAnnotationAdapter(Class<A> cls) {
        for (Map.Entry<Annotation, AnnotationAdapter> a : annotations.entrySet()) {
            if (a.getKey().annotationType() == cls) return a.getValue();
        }
        return null;
    }

    public <A extends Annotation> A getAnnotation(Class<A> cls) {
        for (Annotation a : annotations.keySet()) {
            if (a.annotationType() == cls) return (A) a;
        }
        return null;
    }

    @Override
    public Object process(Object value, CommandContext ctx) throws InvalidArgumentException {
        System.out.println("value of " + this + " before process: " + value);
        for (Map.Entry<Annotation,AnnotationAdapter> a : annotations.entrySet()) {
            Object v = a.getValue().process(value,a.getKey(),this,ctx);
            System.out.println("used annotation adapter " + a.getValue() + " and got " + v);
            if (v != null) {
                value = v;
            }
        }
        System.out.println("value of " + this + " after process: " + value);
        return value;
    }

    public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
        for (Annotation a : annotations.keySet()) {
            if (a.annotationType() == annotationType) return true;
        }
        return false;
    }

    @SafeVarargs
    public final void incompatibleAnnotations(Class<? extends Annotation>... annotations) throws IncompatibleAnnotationException {
        incompatibleAnnotations.add(annotations);
    }
}
