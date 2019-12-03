package juniter.core.model;

public interface DUPDocument extends DUPComponent {

	String toDUPdoc(boolean signed);


	default String toDUPDoc(){
		return toDUP();
	}


}
