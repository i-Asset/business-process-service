package eu.nimble.service.bp.util.persistence.catalogue;

import eu.nimble.service.bp.model.hyperjaxb.DocumentType;
import eu.nimble.service.bp.model.hyperjaxb.ProcessDocumentMetadataDAO;
import eu.nimble.service.bp.model.hyperjaxb.ProcessInstanceDAO;
import eu.nimble.service.bp.util.persistence.bp.ProcessDocumentMetadataDAOUtility;
import eu.nimble.service.bp.swagger.model.Process;
import eu.nimble.service.bp.util.persistence.bp.ProcessInstanceDAOUtility;
import eu.nimble.service.model.ubl.commonaggregatecomponents.*;
import eu.nimble.service.model.ubl.digitalagreement.DigitalAgreementType;
import eu.nimble.service.model.ubl.order.OrderType;
import eu.nimble.service.model.ubl.transportexecutionplanrequest.TransportExecutionPlanRequestType;
import eu.nimble.utility.persistence.JPARepositoryFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by suat on 26-Apr-18.
 */
public class ContractPersistenceUtility {
    private static final String QUERY_GET_BASE_CLAUSE = "SELECT clause FROM ClauseType clause WHERE clause.ID = :clauseId";
    private static final String QUERY_GET_CONTRACT_CLAUSE = "SELECT clause FROM ContractType contract join contract.clause clause WHERE contract.ID = :contractId AND clause.ID = :clauseId";
    private static final String QUERY_GET_DATA_MONITORING_CLAUSE = "SELECT clause FROM DataMonitoringClauseType clause WHERE clause.ID = :clauseId";
    private static final String QUERY_GET_DOCUMENT_CLAUSE = "SELECT clause FROM DocumentClauseType clause WHERE clause.ID = :clauseId";
    private static final String QUERY_CONTRACT_EXISTS = "SELECT count(*) FROM ContractType contract WHERE contract.ID = :contractId";
    private static final String QUERY_GET_CONTRACT = "SELECT contract FROM ContractType contract WHERE contract.ID = :contractId";
    private static final String QUERY_GET_FRAME_CONTRACT_BY_SELLER_BUYER_PRODUCT_IDS =
            "SELECT da FROM DigitalAgreementType da join da.participantParty pp join pp.partyIdentification pid join da.item item" +
                    " WHERE" +
                    " ((pid.ID = :sellerId AND pp.federationInstanceID =:sellerFederationId) OR (pid.ID = :buyerId AND pp.federationInstanceID = :buyerFederationId)) AND " +
                    " item.manufacturersItemIdentification.ID in :itemId" +
                    " GROUP BY da" +
                    " HAVING COUNT(da) = 2 ";
    private static final String QUERY_GET_FRAME_CONTRACTS_BY_PARTY_ID =
            "SELECT da FROM DigitalAgreementType da join da.participantParty pp join pp.partyIdentification pid " +
                    "WHERE pid.ID = :partyId AND pp.federationInstanceID = :federationId ORDER BY da.digitalAgreementTerms.validityPeriod.startDateItem DESC";

    public static ClauseType getBaseClause(String clauseId) {
        ClauseType clauseType = new JPARepositoryFactory().forCatalogueRepository().getSingleEntity(QUERY_GET_BASE_CLAUSE, new String[]{"clauseId"}, new Object[]{clauseId});
        return clauseType;
    }

    public static ClauseType getClause(String clauseId) {
        ClauseType baseClause = getBaseClause(clauseId);
        ClauseType clause = null;
        if (baseClause != null) {
            if (baseClause.getType().contentEquals(eu.nimble.service.model.ubl.extension.ClauseType.DATA_MONITORING.toString())) {
                clause = getDataMonitoringClause(baseClause.getID());

            } else if (baseClause.getType().contentEquals(eu.nimble.service.model.ubl.extension.ClauseType.DOCUMENT.toString())) {
                clause = getDocumentClause(baseClause.getID());
            }
        }
        return clause;
    }

    public static List<ClauseType> getClauses(String documentId, DocumentType documentType, eu.nimble.service.model.ubl.extension.ClauseType clauseType) {
        ContractType contract = null;
        List<ClauseType> clauseTypes = new ArrayList<>();
        if(documentType.equals(DocumentType.ORDER)) {
            OrderType orderType = (OrderType) DocumentPersistenceUtility.getUBLDocument(documentId,DocumentType.ORDER);
            if(orderType.getContract().size() > 0) {
                contract = orderType.getContract().get(0);
            }

        } else if(documentType.equals(DocumentType.TRANSPORTEXECUTIONPLANREQUEST)) {
            TransportExecutionPlanRequestType transportExecutionPlanRequestType = (TransportExecutionPlanRequestType) DocumentPersistenceUtility.getUBLDocument(documentId,DocumentType.TRANSPORTEXECUTIONPLANREQUEST);
            contract = transportExecutionPlanRequestType.getTransportContract();

        }

        if(contract != null) {
            for (ClauseType clause : contract.getClause()) {
                if (clauseType == null || eu.nimble.service.model.ubl.extension.ClauseType.valueOf(clause.getType()) == clauseType) {
                    clauseTypes.add(clause);
                }
            }
        }

        return clauseTypes;
    }

    public static ClauseType getContractClause(String contractId, String clauseId) {
        ClauseType clause = new JPARepositoryFactory().forCatalogueRepository(true).getSingleEntity(QUERY_GET_CONTRACT_CLAUSE, new String[]{"contractId", "clauseId"}, new Object[]{contractId, clauseId});
        return clause;
    }

    public static DataMonitoringClauseType getDataMonitoringClause(String clauseId) {
        DataMonitoringClauseType clause = new JPARepositoryFactory().forCatalogueRepository(true).getSingleEntity(QUERY_GET_DATA_MONITORING_CLAUSE, new String[]{"clauseId"}, new Object[]{clauseId});
        return clause;
    }

    public static DocumentClauseType getDocumentClause(String clauseId) {
        DocumentClauseType clause = new JPARepositoryFactory().forCatalogueRepository(true).getSingleEntity(QUERY_GET_DOCUMENT_CLAUSE, new String[]{"clauseId"}, new Object[]{clauseId});
        return clause;
    }

    public static boolean contractExists(String contractID) {
        int count = ((Long) new JPARepositoryFactory().forCatalogueRepository().getSingleEntity(QUERY_CONTRACT_EXISTS, new String[]{"contractId"}, new Object[]{contractID})).intValue();
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static ContractType getContract(String contractId) {
        ContractType contract = new JPARepositoryFactory().forCatalogueRepository(true).getSingleEntity(QUERY_GET_CONTRACT, new String[]{"contractId"}, new Object[]{contractId});
        return contract;
    }

    /**
     * Starting from the specified {@link ProcessInstanceDAO}, this method traverses the previous
     * process instances until the beginning and add the response document of each process instances as a
     * {@link DocumentClauseType}s.
     *
     * @param processInstanceId
     * @return
     */
    public static ContractType constructContractForProcessInstances(String processInstanceId) {
        ContractType realContract = null;

        ContractType contract = new ContractType();
        contract.setID(UUID.randomUUID().toString());

        boolean negotiationClauseFound = false;
        boolean ppapClauseFound = false;

        List<ProcessInstanceDAO> processInstances = ProcessInstanceDAOUtility.getAllProcessInstancesInCollaborationHistory(processInstanceId);

        for (ProcessInstanceDAO processInstance : processInstances) {

            if (negotiationClauseFound && processInstance.getProcessID().equalsIgnoreCase(Process.ProcessTypeEnum.NEGOTIATION.toString())) {
                continue;
            } else if (ppapClauseFound && processInstance.getProcessID().equalsIgnoreCase(Process.ProcessTypeEnum.PPAP.toString())) {
                continue;
            }

            List<ProcessDocumentMetadataDAO> documents = ProcessDocumentMetadataDAOUtility.findByProcessInstanceID(processInstance.getProcessInstanceID());

            // if the process is completed
            if(documents.size() > 1) {
                ProcessDocumentMetadataDAO docMetadata = documents.get(1);
                ProcessDocumentMetadataDAO reqMetadata = documents.get(0);
                // if the second document has a future submission date
                if (documents.get(0).getSubmissionDate().compareTo(documents.get(1).getSubmissionDate()) > 0) {
                    docMetadata = documents.get(0);
                    reqMetadata = documents.get(1);
                }

                // Check whether a contract already exists or not
                if(reqMetadata.getType().equals(DocumentType.ORDER)){
                    OrderType orderType = (OrderType) DocumentPersistenceUtility.getUBLDocument(reqMetadata.getDocumentID(),DocumentType.ORDER);
                    if(orderType.getContract().size() > 0){
                        realContract = orderType.getContract().get(0);
                    }
                    break;
                }
                else if(reqMetadata.getType().equals(DocumentType.TRANSPORTEXECUTIONPLANREQUEST)){
                    TransportExecutionPlanRequestType transportExecutionPlanRequestType = (TransportExecutionPlanRequestType) DocumentPersistenceUtility.getUBLDocument(reqMetadata.getDocumentID(),DocumentType.TRANSPORTEXECUTIONPLANREQUEST);
                    realContract = transportExecutionPlanRequestType.getTransportContract();
                    break;
                }

                DocumentType documentType = docMetadata.getType();
                if(documentType.equals(DocumentType.ITEMINFORMATIONRESPONSE) ||
                   documentType.equals(DocumentType.QUOTATION) ||
                   documentType.equals(DocumentType.PPAPRESPONSE)) {

                    DocumentClauseType clause = new DocumentClauseType();
                    contract.getClause().add(clause);
                    DocumentReferenceType docRef = new DocumentReferenceType();
                    clause.setClauseDocumentRef(docRef);
                    clause.setType(eu.nimble.service.model.ubl.extension.ClauseType.DOCUMENT.toString());

                    clause.setID(UUID.randomUUID().toString());
                    docRef.setID(docMetadata.getDocumentID());

                    if (docMetadata.getType().equals(DocumentType.ITEMINFORMATIONRESPONSE)) {
                        docRef.setDocumentType(DocumentType.ITEMINFORMATIONRESPONSE.toString());

                    } else if (docMetadata.getType().equals(DocumentType.QUOTATION)) {
                        docRef.setDocumentType(DocumentType.QUOTATION.toString());
                        negotiationClauseFound = true;

                    } else if (docMetadata.getType().equals(DocumentType.PPAPRESPONSE)) {
                        docRef.setDocumentType(DocumentType.PPAPRESPONSE.toString());
                        ppapClauseFound = true;
                    }
                }
            }
        }

        // Add new clauses to the contract
        if(realContract != null){
            for (ClauseType clause : contract.getClause()){
                realContract.getClause().add(clause);
            }
        }
        else {
            return contract;
        }
        return realContract;
    }

    public static DigitalAgreementType getFrameContractAgreementById(String sellerId,String sellerFederationId, String buyerId,String buyerFederationId, String productId) {
        return new JPARepositoryFactory().forCatalogueRepository(true).getSingleEntity(QUERY_GET_FRAME_CONTRACT_BY_SELLER_BUYER_PRODUCT_IDS,
                new String[]{"sellerId","sellerFederationId", "buyerId","buyerFederationId", "itemId"}, new Object[]{sellerId, sellerFederationId, buyerId, buyerFederationId,Arrays.asList(productId)});
    }

    public static List<DigitalAgreementType> getFrameContractAgreementByIds(String sellerId,String sellerFederationId, String buyerId,String buyerFederationId, List<String> productIds) {
        return new JPARepositoryFactory().forCatalogueRepository(true).getEntities(QUERY_GET_FRAME_CONTRACT_BY_SELLER_BUYER_PRODUCT_IDS,
                new String[]{"sellerId","sellerFederationId", "buyerId","buyerFederationId", "itemId"}, new Object[]{sellerId,sellerFederationId, buyerId,buyerFederationId, productIds});
    }

    public static List<DigitalAgreementType> getFrameContractsByPartyId(String partyId, String federationId) {
        return new JPARepositoryFactory().forCatalogueRepository(true).getEntities(QUERY_GET_FRAME_CONTRACTS_BY_PARTY_ID,
                new String[]{"partyId","federationId"}, new Object[]{partyId,federationId});
    }
}
