package juniter.core.model.dbo;

public interface DUPDocument {
	String toDUP();

	default String toDUPDoc(){
		return toDUP();
	}
}
