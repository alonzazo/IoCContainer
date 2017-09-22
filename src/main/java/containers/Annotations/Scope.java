package containers.Annotations;

/**
 * Created by Mariana on 20/09/2017.
 */
public @interface Scope {
    ScopeType scope = ScopeType.singleton;

}
enum ScopeType{
    singleton,prototype
}
