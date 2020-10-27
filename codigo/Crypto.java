package aed.recursion;

import es.upm.aedlib.Pair;
import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.PositionList;
import es.upm.aedlib.positionlist.NodePositionList;

public class Crypto {

	public static void encryptRec(PositionList<Character> key, Position<Character> keyPos, PositionList<Character> text,
			Position<Character> textPos, PositionList<Integer> encodedText, int desp) {

		if (textPos != null && textPos.element() != null && !keyPos.element().equals(textPos.element())) {
			if (keyPos.element().compareTo(textPos.element()) > 0) {
				desp--;
				keyPos = key.prev(keyPos);

			} else {
				desp++;
				keyPos = key.next(keyPos);
			}

			encryptRec(key, keyPos, text, textPos, encodedText, desp);

		} else if (textPos != null) {

			encodedText.addLast(desp);
			textPos = text.next(textPos);
			desp = 0;
			encryptRec(key, keyPos, text, textPos, encodedText, desp);
		}

	}

	public static PositionList<Integer> encrypt(PositionList<Character> key, PositionList<Character> text) {
		PositionList<Integer> encodedText = new NodePositionList<Integer>();
		int desp = 0;
		encryptRec(key, key.first(), text, text.first(), encodedText, desp);

		return encodedText;
	}

	public static void decryptRec(PositionList<Character> key, Position<Character> keyPos,
			PositionList<Integer> encodedText, Position<Integer> encodedTextPos,
			PositionList<Character> decodedText, int desp){
				
		if(encodedTextPos!=null && desp!=0){
			if(desp>0){
				desp--;
				keyPos=key.next(keyPos);
			}else {
				desp++;
				keyPos=key.prev(keyPos);
			}
			decryptRec(key, keyPos, encodedText, encodedTextPos, decodedText, desp);
		}else if(encodedTextPos!=null){

			decodedText.addLast(keyPos.element());
			encodedTextPos=encodedText.next(encodedTextPos);
			
			if(encodedTextPos!=null){
				decryptRec(key, keyPos, encodedText, encodedTextPos, decodedText, encodedTextPos.element());
			}
			
		}
	}

	public static PositionList<Character> decrypt(PositionList<Character> key, PositionList<Integer> encodedText) {
		PositionList<Character> decodedText=new NodePositionList<Character>();
		decryptRec(key, key.first(), encodedText, encodedText.first(), decodedText, encodedText.first().element());
		return decodedText;
	}

}
