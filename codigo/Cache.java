package aed.cache;

import java.util.Iterator;

import es.upm.aedlib.Entry;
import es.upm.aedlib.Position;
import es.upm.aedlib.map.*;
import es.upm.aedlib.positionlist.*;

public class Cache<Key, Value> {

	// Tamano de la cache
	private int maxCacheSize;

	// NO MODIFICA ESTOS ATTRIBUTOS, NI CAMBIA SUS NOMBRES: mainMemory,
	// cacheContents, keyListLRU

	// Para acceder a la memoria M
	private Storage<Key, Value> mainMemory;
	// Un 'map' que asocia una clave con un ``CacheCell''
	private Map<Key, CacheCell<Key, Value>> cacheContents;
	// Una PositionList que guarda las claves en orden de
	// uso -- la clave mas recientemente usado sera el keyListLRU.first()
	private PositionList<Key> keyListLRU;

	// Constructor de la cache. Especifica el tamano maximo
	// y la memoria que se va a utilizar
	public Cache(int maxCacheSize, Storage<Key, Value> mainMemory) {
		this.maxCacheSize = maxCacheSize;

		// NO CAMBIA
		this.mainMemory = mainMemory;
		this.cacheContents = new HashTableMap<Key, CacheCell<Key, Value>>();
		this.keyListLRU = new NodePositionList<Key>();
	}

	private boolean estaEnCache(Key key) {
		Position<Key> cursor = keyListLRU.first();
		boolean esta = false;
		if (!keyListLRU.isEmpty()) {
			while (cursor != null && !esta) {
				if (cursor.element().equals(key)) {
					esta = true;
				}
				cursor = keyListLRU.next(cursor);
			}
		}
		return esta;
	}

	// Devuelve el valor que corresponde a una clave "Key"
	public Value get(Key key) {
		// CAMBIA este metodo
		Value valor = null;
		CacheCell<Key, Value> celda = null;

		if (keyListLRU.size() < maxCacheSize) {
			if (estaEnCache(key)) {
				celda = cacheContents.get(key);
				valor = celda.getValue();
				keyListLRU.remove(celda.getPos());
				keyListLRU.addFirst(key);
				celda.setPos(keyListLRU.first());

			} else {
				valor = mainMemory.read(key);
				if (valor != null) {
					keyListLRU.addFirst(key);
					cacheContents.put(key, new CacheCell<Key, Value>(valor, false, keyListLRU.first()));
				}
			}
		} else {

			if (estaEnCache(key)) {
				celda = cacheContents.get(key);
				valor = celda.getValue();
				keyListLRU.remove(celda.getPos());
				keyListLRU.addFirst(key);
				celda.setPos(keyListLRU.first());

			} else {

        
				valor = mainMemory.read(key);
        Key lastKey=null;
				lastKey = keyListLRU.last().element();
				celda = cacheContents.get(lastKey);
				
				  
        if (mainMemory.read(lastKey)==null) {
					mainMemory.write(lastKey, celda.getValue());
				}        
				if (valor != null) {
          if (celda.getDirty()) {
					mainMemory.write(lastKey, celda.getValue());
				}
          cacheContents.remove(lastKey);
          keyListLRU.remove(celda.getPos());  
					keyListLRU.addFirst(key);
					cacheContents.put(key, new CacheCell<Key, Value>(valor, false, keyListLRU.first()));
        }
      }
		}
		return valor;
	}

	// Establece un valor nuevo para la clave en la memoria cache
	public void put(Key key, Value value) {
		// CAMBIA este metodo
    CacheCell<Key, Value> celda = null;
    if(get(key)!=null){
      celda=cacheContents.get(key);
      if(!value.equals(celda.getValue())){
        celda.setDirty(true);
      }
      celda.setValue(value);
    } else if(!keyListLRU.isEmpty()){
      
      Key lastKey = keyListLRU.last().element();
      celda = cacheContents.get(lastKey);
      
      
      if(keyListLRU.size()==maxCacheSize){
        cacheContents.remove(lastKey);
        keyListLRU.remove(celda.getPos());
        if (celda.getDirty()) {
          mainMemory.write(lastKey, celda.getValue());
        }
      }
      keyListLRU.addFirst(key);
      cacheContents.put(key, new CacheCell<Key, Value>(value, false, keyListLRU.first()));
    }
    else{
      keyListLRU.addFirst(key);
      cacheContents.put(key,new CacheCell<Key,Value>(value,false,keyListLRU.first()));
    }
	}

	// NO CAMBIA
	public String toString() {
		return "cache";
	}
}
