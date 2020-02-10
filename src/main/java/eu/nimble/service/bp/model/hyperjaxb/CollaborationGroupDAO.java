//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.09.16 at 03:08:56 PM EET 
//


package eu.nimble.service.bp.model.hyperjaxb;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * <p>Java class for CollaborationGroupDAO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CollaborationGroupDAO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="associatedProcessInstanceGroups" type="{}ProcessInstanceGroupDAO" maxOccurs="unbounded"/&gt;
 *         &lt;element name="archived" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="status" type="{}CollaborationStatus"/&gt;
 *         &lt;element name="isProject" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="federatedCollaborationGroupMetadatas" type="{}FederatedCollaborationGroupMetadataDAO" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CollaborationGroupDAO", propOrder = {
    "name",
    "associatedProcessInstanceGroups",
    "archived",
    "status",
    "isProject",
    "federatedCollaborationGroupMetadatas"
})
@Entity(name = "CollaborationGroupDAO")
@Table(name = "COLLABORATION_GROUP_DAO")
@Inheritance(strategy = InheritanceType.JOINED)
public class CollaborationGroupDAO
    implements Equals
{

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected List<ProcessInstanceGroupDAO> associatedProcessInstanceGroups;
    protected boolean archived;
    @XmlElement(required = true)
    @XmlSchemaType(name = "token")
    protected CollaborationStatus status;
    protected boolean isProject;
    @XmlElement(required = true)
    protected List<FederatedCollaborationGroupMetadataDAO> federatedCollaborationGroupMetadatas;
    @XmlAttribute(name = "Hjid")
    protected Long hjid;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "NAME_", length = 255)
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the associatedProcessInstanceGroups property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the associatedProcessInstanceGroups property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssociatedProcessInstanceGroups().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProcessInstanceGroupDAO }
     * 
     * 
     */
    @OneToMany(targetEntity = ProcessInstanceGroupDAO.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "ASSOCIATED_PROCESS_INSTANCE__1")
    public List<ProcessInstanceGroupDAO> getAssociatedProcessInstanceGroups() {
        if (associatedProcessInstanceGroups == null) {
            associatedProcessInstanceGroups = new ArrayList<ProcessInstanceGroupDAO>();
        }
        return this.associatedProcessInstanceGroups;
    }

    /**
     * 
     * 
     */
    public void setAssociatedProcessInstanceGroups(List<ProcessInstanceGroupDAO> associatedProcessInstanceGroups) {
        this.associatedProcessInstanceGroups = associatedProcessInstanceGroups;
    }

    /**
     * Gets the value of the archived property.
     * 
     */
    @Basic
    @Column(name = "ARCHIVED")
    public boolean isArchived() {
        return archived;
    }

    /**
     * Sets the value of the archived property.
     * 
     */
    public void setArchived(boolean value) {
        this.archived = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link CollaborationStatus }
     *     
     */
    @Basic
    @Column(name = "STATUS", length = 255)
    @Enumerated(EnumType.STRING)
    public CollaborationStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link CollaborationStatus }
     *     
     */
    public void setStatus(CollaborationStatus value) {
        this.status = value;
    }

    /**
     * Gets the value of the isProject property.
     * 
     */
    @Basic
    @Column(name = "IS_PROJECT")
    public boolean isIsProject() {
        return isProject;
    }

    /**
     * Sets the value of the isProject property.
     * 
     */
    public void setIsProject(boolean value) {
        this.isProject = value;
    }

    /**
     * Gets the value of the federatedCollaborationGroupMetadatas property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the federatedCollaborationGroupMetadatas property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFederatedCollaborationGroupMetadatas().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FederatedCollaborationGroupMetadataDAO }
     * 
     * 
     */
    @OneToMany(targetEntity = FederatedCollaborationGroupMetadataDAO.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "FEDERATED_COLLABORATION_GROU_1")
    public List<FederatedCollaborationGroupMetadataDAO> getFederatedCollaborationGroupMetadatas() {
        if (federatedCollaborationGroupMetadatas == null) {
            federatedCollaborationGroupMetadatas = new ArrayList<FederatedCollaborationGroupMetadataDAO>();
        }
        return this.federatedCollaborationGroupMetadatas;
    }

    /**
     * 
     * 
     */
    public void setFederatedCollaborationGroupMetadatas(List<FederatedCollaborationGroupMetadataDAO> federatedCollaborationGroupMetadatas) {
        this.federatedCollaborationGroupMetadatas = federatedCollaborationGroupMetadatas;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if ((object == null)||(this.getClass()!= object.getClass())) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final CollaborationGroupDAO that = ((CollaborationGroupDAO) object);
        {
            String lhsName;
            lhsName = this.getName();
            String rhsName;
            rhsName = that.getName();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "name", lhsName), LocatorUtils.property(thatLocator, "name", rhsName), lhsName, rhsName)) {
                return false;
            }
        }
        {
            List<ProcessInstanceGroupDAO> lhsAssociatedProcessInstanceGroups;
            lhsAssociatedProcessInstanceGroups = (((this.associatedProcessInstanceGroups!= null)&&(!this.associatedProcessInstanceGroups.isEmpty()))?this.getAssociatedProcessInstanceGroups():null);
            List<ProcessInstanceGroupDAO> rhsAssociatedProcessInstanceGroups;
            rhsAssociatedProcessInstanceGroups = (((that.associatedProcessInstanceGroups!= null)&&(!that.associatedProcessInstanceGroups.isEmpty()))?that.getAssociatedProcessInstanceGroups():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "associatedProcessInstanceGroups", lhsAssociatedProcessInstanceGroups), LocatorUtils.property(thatLocator, "associatedProcessInstanceGroups", rhsAssociatedProcessInstanceGroups), lhsAssociatedProcessInstanceGroups, rhsAssociatedProcessInstanceGroups)) {
                return false;
            }
        }
        {
            boolean lhsArchived;
            lhsArchived = this.isArchived();
            boolean rhsArchived;
            rhsArchived = that.isArchived();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "archived", lhsArchived), LocatorUtils.property(thatLocator, "archived", rhsArchived), lhsArchived, rhsArchived)) {
                return false;
            }
        }
        {
            CollaborationStatus lhsStatus;
            lhsStatus = this.getStatus();
            CollaborationStatus rhsStatus;
            rhsStatus = that.getStatus();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "status", lhsStatus), LocatorUtils.property(thatLocator, "status", rhsStatus), lhsStatus, rhsStatus)) {
                return false;
            }
        }
        {
            boolean lhsIsProject;
            lhsIsProject = this.isIsProject();
            boolean rhsIsProject;
            rhsIsProject = that.isIsProject();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "isProject", lhsIsProject), LocatorUtils.property(thatLocator, "isProject", rhsIsProject), lhsIsProject, rhsIsProject)) {
                return false;
            }
        }
        {
            List<FederatedCollaborationGroupMetadataDAO> lhsFederatedCollaborationGroupMetadatas;
            lhsFederatedCollaborationGroupMetadatas = (((this.federatedCollaborationGroupMetadatas!= null)&&(!this.federatedCollaborationGroupMetadatas.isEmpty()))?this.getFederatedCollaborationGroupMetadatas():null);
            List<FederatedCollaborationGroupMetadataDAO> rhsFederatedCollaborationGroupMetadatas;
            rhsFederatedCollaborationGroupMetadatas = (((that.federatedCollaborationGroupMetadatas!= null)&&(!that.federatedCollaborationGroupMetadatas.isEmpty()))?that.getFederatedCollaborationGroupMetadatas():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "federatedCollaborationGroupMetadatas", lhsFederatedCollaborationGroupMetadatas), LocatorUtils.property(thatLocator, "federatedCollaborationGroupMetadatas", rhsFederatedCollaborationGroupMetadatas), lhsFederatedCollaborationGroupMetadatas, rhsFederatedCollaborationGroupMetadatas)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    /**
     * Gets the value of the hjid property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    @Id
    @Column(name = "HJID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getHjid() {
        return hjid;
    }

    /**
     * Sets the value of the hjid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setHjid(Long value) {
        this.hjid = value;
    }

}
