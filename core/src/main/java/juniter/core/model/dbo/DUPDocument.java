package juniter.core.model.dbo;

public interface DUPDocument extends DUPComponent {

	String toDUPdoc(boolean signed);


	default String toDUPDoc(){
		return toDUP();
	}


}
