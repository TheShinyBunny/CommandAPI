package com.shinybunny.cmdapi.arguments;

import com.shinybunny.cmdapi.CommandAPI;
import com.shinybunny.cmdapi.CommandContext;
import com.shinybunny.cmdapi.annotations.AnnotationAdapter;
import com.shinybunny.cmdapi.exceptions.CommandParseException;
import com.shinybunny.cmdapi.exceptions.IncompatibleAnnotationException;
import com.shinybunny.cmdapi.exceptions.NoAdapterFoundException;
import com.shinybunny.cmdapi.utils.InputReader;
import com.shinybunny.cmdapi.exceptions.InvalidArgumentException;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an argument of a command. An argument is created for each parameter of a method.<br/>
 * An argument is created as the sub command argument in tree commands.
 */
public class Argument {

    private String name;

    /**
     * The type of data the argument accepts - the type of its respective parameter.
     */
    private Class<?> type;
    private String description;
    private Map<Annotation,AnnotationAdapter> annotations;
    private ArgumentAdapter<?> adapter;
    private boolean required = true;
    private Object defaultValue;
    private boolean syntax = true;

    public Argument(String name, Class<?> type, Annotation[] annotations, ArgumentAdapter<?> argumentAdapter) throws NoAdapterFoundException, IncompatibleAnnotationException {
        this.name = name;
        if (argumentAdapter == null) {
            throw new NoAdapterFoundException("No argument adapter found for type " + type,type);
        }
        this.adapter = argumentAdapter;
        this.type = type;
        this.annotations = new HashMap<>();
        this.defaultValue = adapter.getDefault();
        this.addAnnotationAdapters(annotations);
    }

    private void addAnnotationAdapters(Annotation[] annotations) throws IncompatibleAnnotationException, NoAdapterFoundException {
        for (Annotation a : annotations) {
            AnnotationAdapter adapter = CommandAPI.getAnnotationAdapter(a.annotationType());
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
                // call the init() method of the adapter, to modify this argument according to the annotation.
                adapter.init(this, a);
            } else {
                throw new NoAdapterFoundException("No adapter found for annotation " + a.annotationType(),a.annotationType());
            }
        }
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public Object parse(InputReader reader, CommandContext ctx) throws CommandParseException {
        return adapter.parse(reader,this,ctx);
    }

    public Object getOutOfSyntax(CommandContext ctx) {
        return adapter.outOfSyntax(ctx);
    }

    public Object process(Object value, CommandContext ctx) throws InvalidArgumentException {
        for (Map.Entry<Annotation,AnnotationAdapter> a : annotations.entrySet()) {
            Object v = a.getValue().process(value,a.getKey(),this,ctx);
            if (v != null) {
                value = v;
            }
        }
        return value;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public boolean isNullable() {
        return adapter.nullable();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name + " (" + type.getSimpleName().toLowerCase() + ")";
    }

    public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
        for (Annotation a : annotations.keySet()) {
            if (a.annotationType() == annotationType) return true;
        }
        return false;
    }

    public boolean isSyntax() {
        return syntax;
    }
}
