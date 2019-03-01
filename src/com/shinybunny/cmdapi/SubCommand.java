package com.shinybunny.cmdapi;

import com.shinybunny.cmdapi.exceptions.IncompatibleAnnotationException;
import com.shinybunny.cmdapi.exceptions.NoAdapterFoundException;

import java.lang.reflect.Method;

public class SubCommand extends Command {
    public SubCommand(String name, Method method, Object holder) throws IncompatibleAnnotationException, NoAdapterFoundException {
        super(name, method, holder);
    }
}
