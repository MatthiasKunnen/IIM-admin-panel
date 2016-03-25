package gui.controls;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MethodBuilder {
    private Object source;
    private Object defaultValue = null;
    private List<MethodCall> callList = new ArrayList<>();
    private MethodCall targetCall;
    private Object targetObject;

    public MethodBuilder(Object source) {
        this.source = source;
    }

    public MethodBuilder setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public MethodBuilder addMethods(String... methods) {
        for (String method : methods) {
            callList.add(new MethodCall(method));
        }
        return this;
    }

    public MethodBuilder addMethod(String method, Object... parameters) {
        callList.add(new MethodCall(method, parameters));
        return this;
    }

    public MethodBuilder setTarget(Object targetObject, String method, Object... parameters) {
        this.targetCall = new MethodCall(method, parameters);
        this.targetObject = targetObject;
        return this;
    }

    public Object run() {
        Object result = this.source;
        for (MethodCall m : this.callList) {
            result = invoke(result, m.getName(), m.getParameters());
            if (result == null) {
                return defaultValue;
            }
        }
        if (targetCall != null && targetObject != null) {
            List<Object> parameters = new ArrayList<>(Arrays.asList(targetCall.getParameters()));
            parameters.add(0, result);
            invoke(targetObject, targetCall.getName(), parameters.toArray(new Object[parameters.size()]));
        }
        return result;
    }

    private Object invoke(Object obj, String method, Object... parameters) {
        try {
            if (parameters.length == 0) {
                return obj.getClass().getMethod(method).invoke(obj);
            } else {
                List<Class<?>> typeList = Arrays.stream(parameters).map(Object::getClass).collect(Collectors.toList());
                Class[] types = typeList.toArray(new Class[typeList.size()]);
                return obj.getClass().getMethod(method, types).invoke(obj, parameters);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return this.defaultValue;
        }
    }


    private class MethodCall {
        private String name;
        private Object[] parameters;

        public MethodCall(String name, Object... parameters) {
            this.name = name;
            this.parameters = parameters;
        }

        public String getName() {
            return name;
        }

        public Object[] getParameters() {
            return parameters;
        }
    }
}
