/*
 * component:   "openEHR Reference Implementation"
 * description: "Class CAttribute"
 * keywords:    "archetype"
 *
 * author:      "Rong Chen <rong@acode.se>"
 * support:     "Acode HB <support@acode.se>"
 * copyright:   "Copyright (c) 2004 Acode HB, Sweden"
 * license:     "See notice at bottom of class"
 *
 * file:        "$URL: http://svn.openehr.org/ref_impl_java/TRUNK/libraries/src/java/org/openehr/am/archetype/constraintmodel/CAttribute.java $"
 * revision:    "$LastChangedRevision: 2 $"
 * last_change: "$LastChangedDate: 2005-10-12 23:20:08 +0200 (Wed, 12 Oct 2005) $"
 */
package org.openehr.am.archetype.constraintmodel;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class represents a constraint on any kind of attribute node.
 *
 * @author Rong Chen
 * @version 1.0
 */
public abstract class CAttribute extends ArchetypeConstraint {

    /**
     * enumeration of attribute existence
     */
    public enum Existence {
        REQUIRED, OPTIONAL, NOT_ALLOWED
    };

    /**
     * Constructs an AttributeConstraint
     *
     * @param path
     * @param rmAttributeName
     * @param existence
     * @param children        List<CObject>
     * @throws IllegalArgumentException if rmAttributeName empty
     *                                  or existence null or children null
     */
    public CAttribute(String path, String rmAttributeName,
                      Existence existence, List<CObject> children) {

        super(children == null, path);

        if (StringUtils.isEmpty(rmAttributeName)) {
            throw new IllegalArgumentException("rmTypeName null");
        }
        if (existence == null) {
            throw new IllegalArgumentException("existence null");
        }
        this.rmAttributeName = rmAttributeName;
        this.existence = existence;
        this.children = new ArrayList<CObject>();
        if(children != null) {
        	this.children.addAll(children);
        }
    }
    
    public abstract CAttribute copy();
    
    protected List<CObject> copyChildren() {
    	
    	if(children == null) {
    		return null;
    	}
    	List<CObject> list = new ArrayList<CObject>();
    	for(CObject cobj : children) {
    		if(cobj == null) {
    			System.out.println("null cobj while copying c_attr: " + rmAttributeName + ", " + path());    			
    		} else {
                list.add(cobj.copy());
            }
    	}    	
    	return list;
    }

    /**
     * Creates a CAttribute without child
     * 
     * @param path
     * @param rmAttributeName
     * @param existence
     */
    public CAttribute(String path, String rmAttributeName,
            Existence existence) {
    	this(path, rmAttributeName, existence, null);
    }
    
    /**
     * Reference model attribute within the enclosing type represented by
     * a object constraint
     *
     * @return reference model attribute name
     */
    public String getRmAttributeName() {
        return rmAttributeName;
    }

    /**
     * Existence of this attribute constaint
     *
     * @return existence
     */
    public Existence getExistence() {
        return existence;
    }

    /**
     * Returns true if this attribute is required
     *
     * @return true if required
     */
    public boolean isRequired() {
        return Existence.REQUIRED.equals(existence);
    }
    
    /**
     * Returns true if this attribute should be allowed
     * 
     * @return
     */
    public boolean isAllowed() {
    	return ! Existence.NOT_ALLOWED.equals(existence);
    }

    /**
     * Return true if this attribute is required
     * or there is any submitted input value for its descendants
     *
     * @return true if required
     */
    public boolean isRequired(Set<String> paths) {
        if (isRequired()) {
            return true;
        }

        // if any submitted value for descendant nodes
        for (String path : paths) {
            if (path.startsWith(childNodePathBase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * List of children object constraint
     *
     * @return  List<CObject> or null if not present
     */
    public List<CObject> getChildren() {
        return children;
    }
    
    /**
     * Adds a child to the children list
     * 
     * @param child not null
     */
    public void addChild(CObject child) {
    	if(child == null) {
    		throw new IllegalArgumentException("null child");
    	}
    	children.add(child);
    	child.setParent(this);
    }    
    
    public void removeChild(CObject child) {
    	children.remove(child);
    }
    
    public void removeAllChildren() {
    	children.clear();
    }

    /**
     * True if constraints represented by other are narrower than this node.
     *
     * @param constraint
     * @return true if subset
     * @throws IllegalArgumentException if constraint null
     */
    public abstract boolean isSubsetOf(ArchetypeConstraint constraint);

    /**
     * Return path of parent object node
     *
     * @return parent node path
     */
    public String parentNodePath() {

        // remove both "/"  and attribute name
        return path().substring(0,
                path().length() - rmAttributeName.length() - 1);
    }

    /**
     * Return path base for the children nodes
     *
     * @return child node path base
     */
    public String childNodePathBase() {
        return parentNodePath() + PATH_SEPARATOR + rmAttributeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CAttribute)) return false;
        if (!super.equals(o)) return false;

        CAttribute that = (CAttribute) o;

        if (getRmAttributeName() != null ? !getRmAttributeName().equals(that.getRmAttributeName()) : that.getRmAttributeName() != null)
            return false;
        if (getExistence() != that.getExistence()) return false;
        return getChildren() != null ? getChildren().equals(that.getChildren()) : that.getChildren() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getRmAttributeName() != null ? getRmAttributeName().hashCode() : 0);
        result = 31 * result + (getExistence() != null ? getExistence().hashCode() : 0);
        result = 31 * result + (getChildren() != null ? getChildren().hashCode() : 0);
        return result;
    }

    /* fields */
    private final String rmAttributeName;
    private final Existence existence;
    final List<CObject> children;
}

/*
 *  ***** BEGIN LICENSE BLOCK *****
 *  Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 *  The contents of this file are subject to the Mozilla Public License Version
 *  1.1 (the 'License'); you may not use this file except in compliance with
 *  the License. You may obtain a copy of the License at
 *  http://www.mozilla.org/MPL/
 *
 *  Software distributed under the License is distributed on an 'AS IS' basis,
 *  WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 *  for the specific language governing rights and limitations under the
 *  License.
 *
 *  The Original Code is CAttribute.java
 *
 *  The Initial Developer of the Original Code is Rong Chen.
 *  Portions created by the Initial Developer are Copyright (C) 2003-2007
 *  the Initial Developer. All Rights Reserved.
 *
 *  Contributor(s): Bert Verhees
 *
 * Software distributed under the License is distributed on an 'AS IS' basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 *  ***** END LICENSE BLOCK *****
 */