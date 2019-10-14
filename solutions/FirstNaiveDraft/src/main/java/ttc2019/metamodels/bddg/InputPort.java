/**
 */
package ttc2019.metamodels.bddg;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Input Port</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link InputPort#getSubtrees <em>Subtrees</em>}</li>
 * </ul>
 *
 * @see BDDGPackage#getInputPort()
 * @model
 * @generated
 */
public interface InputPort extends Port {
	/**
	 * Returns the value of the '<em><b>Subtrees</b></em>' reference list.
	 * The list contents are of type {@link Subtree}.
	 * It is bidirectional and its opposite is '{@link Subtree#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Subtrees</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Subtrees</em>' reference list.
	 * @see BDDGPackage#getInputPort_Subtrees()
	 * @see Subtree#getPort
	 * @model opposite="port" ordered="false"
	 * @generated
	 */
	EList<Subtree> getSubtrees();

} // InputPort
