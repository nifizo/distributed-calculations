package org.example.dao.xml;

import org.example.dao.CrudDao;
import org.example.model.IId;
import org.example.xml.dom.reader.Reader;
import org.example.xml.dom.writer.Writer;

import javax.xml.validation.Schema;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @param <C> - container, that contains list of target objects
 * @param <T> - target object generic
 * @param <Id> - id generic
 */
public abstract class XmlDao<C, T extends IId<Id>, Id> implements CrudDao<T, Id> {
    protected final String inputFileXML;
    protected final Reader<C> reader;
    protected final Writer<C> writer;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Schema schema;
    public XmlDao(String inputFileXML, Reader<C> reader, Writer<C> writer, Schema schema) {
        this.inputFileXML = inputFileXML;
        this.reader = reader;
        this.writer = writer;
        this.schema = schema;
    }
    protected void writeFile(C parsed) throws Exception {
        try {
            lock.writeLock().lock();
            writer.write(inputFileXML, parsed);
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected C readFile() throws Exception {
        C result = null;
        try {
            lock.readLock().lock();
            result =  reader.read(inputFileXML, schema);
        } finally {
            lock.readLock().unlock();
        }
        return result;
    }

    protected abstract List<T> toCollection(C container);

    protected  <V> V withReadWrite(Function<C, V> function) throws Exception {
        V result = null;
        try {
            lock.writeLock().lock();
            C container = readFile();
            result =  function.apply(container);
            writeFile(container);
        } finally {
            lock.writeLock().unlock();
        }
        return result;
    }

    private void withReadWrite(Consumer<C> consumer) throws Exception {
        try {
            lock.writeLock().lock();
            C container = readFile();
            consumer.accept(container);
            writeFile(container);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public List<T> findAll() throws Exception {
        List<T> result = null;
        try {
            lock.readLock().lock();
            result = toCollection(readFile());
        } finally {
            lock.readLock().unlock();
        }
        return result;
    }

    @Override
    public T create(T object) throws Exception {
        return withReadWrite(parsed->{
            List<T> containersList = toCollection(parsed);
            containersList.add(object);
            return object;
        });
    }

    @Override
    public T read(Id id) throws Exception {
        var res = findAll().stream().filter(d->id.equals(d.getId())).findFirst();
        if (res.isPresent()) {
            return res.get();
        }
        else{
            throw new NoSuchElementException("Element with id " + id + " not found");
        }
    }

    @Override
    public void update(T object) throws Exception {
        withReadWrite(parsed -> {
            List<T> objectList = toCollection(parsed);
            Id targetId = object.getId();
            if (targetId==null){
                throw new IllegalArgumentException("Not initialized element passed to update");
            }
            deleteById(objectList, targetId);
            objectList.add(object);
        });
    }

    private void deleteById(List<T> objectList, Id targetId) {
        if(!objectList.removeIf(d-> Objects.equals(d.getId(), targetId))){
            throw new NoSuchElementException("Called to remove not existing element");
        }
    }

    public void delete(T object) throws Exception {
        withReadWrite(container -> {
            if (!toCollection(container).remove(object)) {
                throw new NoSuchElementException("Called to not existing element");
            }
        });
    }

    @Override
    public void delete(Id id) throws Exception {
        withReadWrite(container -> {
            try {
                deleteById(toCollection(container), id);
            } catch (NoSuchElementException e) {
                throw new NoSuchElementException("Called to not existing element");
            }
        });
    }
}
