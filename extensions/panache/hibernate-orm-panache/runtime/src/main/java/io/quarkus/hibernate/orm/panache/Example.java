package io.quarkus.hibernate.orm.panache;

public class Example<T> {
    private Class<T> clazz;

    public static <T> Example of(Class<T> clazz){
        Example example = new Example();
        example.setClazz(clazz);
        return example;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }
}
