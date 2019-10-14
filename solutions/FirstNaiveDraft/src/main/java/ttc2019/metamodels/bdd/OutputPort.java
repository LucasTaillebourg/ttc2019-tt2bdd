/**
 */
package ttc2019.metamodels.bdd;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Output Port</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link OutputPort#getAssignments <em>Assignments</em>}</li>
 * </ul>
 *
 * @see BDDPackage#getOutputPort()
 * @model
 * @generated
 */
public interface OutputPort extends Port {
	/**
	 * Returns the value of the '<em><b>Assignments</b></em>' reference list.
	 * The list contents are of type {@link Assignment}.
	 * It is bidirectional and its opposite is '{@link Assignment#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Assignments</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Assignments</em>' reference list.
	 * @see BDDPackage#getOutputPort_Assignments()
	 * @see Assignment#getPort
	 * @model opposite="port" ordered="false"
	 * @generated
	 */
	EList<Assignment> getAssignments();

} // OutputPort
