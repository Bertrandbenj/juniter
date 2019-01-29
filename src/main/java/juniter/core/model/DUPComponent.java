package juniter.core.model;

public interface DUPComponent {
	String toDUP();

	default String toDUPDoc(){
		return toDUP();
	}
}
