package aed.recursion;

import java.util.Iterator;

import es.upm.aedlib.Pair;
import es.upm.aedlib.positionlist.*;


public class Explorador {

  public static void exploraRec(Pair<Object,PositionList<Lugar>> res, Iterator<Lugar> it, Lugar lugarActual){
    

    if(lugarActual.tieneTesoro()) {
    	res.setLeft(lugarActual.getTesoro());
    }
    if(!lugarActual.sueloMarcadoConTiza()) {
    	lugarActual.marcaSueloConTiza();
    }
    else {
    	return;
    }

    it=lugarActual.caminos().iterator();    
    
    while(res.getLeft()==null && it.hasNext()) {
    	exploraRec(res, it, it.next());
    }
    if(res.getLeft()!=null)
    	res.getRight().addFirst(lugarActual); 
    

  }
  
  public static Pair<Object,PositionList<Lugar>> explora(Lugar inicialLugar) {
    Object tesoro=null;
    PositionList<Lugar> camino=new NodePositionList<Lugar>();
    Iterator<Lugar> it=null;
    Pair<Object,PositionList<Lugar>> resultado= new Pair<Object,PositionList<Lugar>>(tesoro,camino);
    exploraRec(resultado,it, inicialLugar);
    
    
    
    return resultado;

  }
} 
