package juniter.core.model.business;

public interface DUPComponent {
	String toDUP();

	default String toDUPDoc(){
		return toDUP();
	}
}
