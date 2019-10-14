/**
 */
package ttc2019.metamodels.bddg;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Leaf</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link Leaf#getAssignments <em>Assignments</em>}</li>
 * </ul>
 *
 * @see BDDGPackage#getLeaf()
 * @model
 * @generated
 */
public interface Leaf extends Tree {
	/**
	 * Returns the value of the '<em><b>Assignments</b></em>' containment reference list.
	 * The list contents are of type {@link Assignment}.
	 * It is bidirectional and its opposite is '{@link Assignment#getOwner <em>Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Assignments</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Assignments</em>' containment reference list.
	 * @see BDDGPackage#getLeaf_Assignments()
	 * @see Assignment#getOwner
	 * @model opposite="owner" containment="true" required="true" ordered="false"
	 * @generated
	 */
	EList<Assignment> getAssignments();

} // Leaf
