package eu.nimble.service.bp.impl;

import eu.nimble.service.bp.hyperjaxb.model.ProcessInstanceGroupDAO;
import eu.nimble.service.bp.impl.util.persistence.DAOUtility;
import eu.nimble.service.bp.impl.util.persistence.HibernateSwaggerObjectMapper;
import eu.nimble.service.bp.impl.util.persistence.HibernateUtilityRef;
import eu.nimble.service.bp.impl.util.persistence.ProcessInstanceGroupDAOUtility;
import eu.nimble.service.bp.swagger.api.GroupApi;
import eu.nimble.service.bp.swagger.model.ProcessInstance;
import eu.nimble.service.bp.swagger.model.ProcessInstanceGroup;
import eu.nimble.service.bp.swagger.model.ProcessInstanceGroupFilter;
import eu.nimble.service.bp.swagger.model.ProcessInstanceGroupResponse;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suat on 06-Feb-18.
 */
@Controller
public class ProcessInstanceGroupController implements GroupApi {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ResponseEntity<ProcessInstanceGroup> addProcessInstanceToGroup(
            @ApiParam(value = "Identifier of the process instance group to which a new process instance id is added", required = true) @PathVariable("ID") String ID,
            @ApiParam(value = "Identifier of the process instance to be added", required = true) @RequestParam(value = "processInstanceID", required = true) String processInstanceID) {
        logger.debug("Adding process instance: {} to ProcessInstanceGroup: {}", ID);

        ProcessInstanceGroupDAO processInstanceGroupDAO = DAOUtility.getProcessInstanceGroupDAO(ID);
        processInstanceGroupDAO.getProcessInstanceIDs().add(processInstanceID);
        HibernateUtilityRef.getInstance("bp-data-model").update(processInstanceGroupDAO);

        ProcessInstanceGroup processInstanceGroup = HibernateSwaggerObjectMapper.createProcessInstanceGroup(processInstanceGroupDAO);
        ResponseEntity response = ResponseEntity.status(HttpStatus.OK).body(processInstanceGroup);
        logger.debug("Added process instance: {} to ProcessInstanceGroup: {}", ID);
        return response;
    }

    @Override
    public ResponseEntity<ProcessInstanceGroup> deleteProcessInstanceFromGroup(@ApiParam(value = "Identifier of the process instance group from which the process instance id is deleted", required = true) @PathVariable("ID") String ID, @ApiParam(value = "Identifier of the process instance to be deleted", required = true) @RequestParam(value = "processInstanceID", required = true) String processInstanceID) {
        logger.debug("Deleting process instance: {} from ProcessInstanceGroup: {}", ID);

        ProcessInstanceGroupDAO processInstanceGroupDAO = DAOUtility.getProcessInstanceGroupDAO(ID);
        processInstanceGroupDAO.getProcessInstanceIDs().remove(processInstanceID);
        HibernateUtilityRef.getInstance("bp-data-model").update(processInstanceGroupDAO);

        ProcessInstanceGroup processInstanceGroup = HibernateSwaggerObjectMapper.createProcessInstanceGroup(processInstanceGroupDAO);
        ResponseEntity response = ResponseEntity.status(HttpStatus.OK).body(processInstanceGroup);
        logger.debug("Deleted process instance: {} from ProcessInstanceGroup: {}", ID);
        return response;
    }

    @Override
    public ResponseEntity<List<ProcessInstance>> getProcessInstancesOfGroup(@ApiParam(value = "Identifier of the process instance group for which the associated process instances will be retrieved", required = true) @PathVariable("ID") String ID) {
        logger.debug("Getting ProcessInstances for group: {}", ID);

        ProcessInstanceGroupDAO processInstanceGroupDAO = DAOUtility.getProcessInstanceGroupDAO(ID);

        ProcessInstanceGroup processInstanceGroup = HibernateSwaggerObjectMapper.createProcessInstanceGroup(processInstanceGroupDAO);
        ResponseEntity response = ResponseEntity.status(HttpStatus.OK).body(processInstanceGroup);
        logger.debug("Retrieved ProcessInstances for group: {}", ID);
        return response;
    }

    @Override
    public ResponseEntity<Void> deleteProcessInstanceGroup(@ApiParam(value = "", required = true) @PathVariable("ID") String ID) {
        logger.debug("Deleting ProcessInstanceGroup ID: {}", ID);

        DAOUtility.deleteProcessInstanceGroupDAOByID(ID);

        ResponseEntity response = ResponseEntity.status(HttpStatus.OK).body("true");
        logger.debug("Deleted ProcessInstanceGroups: {}", ID);
        return response;
    }

    @Override
    public ResponseEntity<ProcessInstanceGroup> getProcessInstanceGroup(@ApiParam(value = "", required = true) @PathVariable("ID") String ID) {
        logger.debug("Getting ProcessInstanceGroup: {}", ID);

        ProcessInstanceGroupDAO processInstanceGroupDAO = DAOUtility.getProcessInstanceGroupDAO(ID);

        ProcessInstanceGroup processInstanceGroup = HibernateSwaggerObjectMapper.createProcessInstanceGroup(processInstanceGroupDAO);
        ResponseEntity response = ResponseEntity.status(HttpStatus.OK).body(processInstanceGroup);
        logger.debug("Retrieved ProcessInstanceGroup: {}", ID);
        return response;
    }

    @Override
    public ResponseEntity<ProcessInstanceGroupResponse> getProcessInstanceGroups(@ApiParam(value = "Identifier of the party") @RequestParam(value = "partyID", required = false) String partyID,
                                                                                 @ApiParam(value = "Related products") @RequestParam(value = "relatedProducts", required = false) List<String> relatedProducts,
                                                                                 @ApiParam(value = "Related product categories") @RequestParam(value = "relatedProductCategories", required = false) List<String> relatedProductCategories,
                                                                                 @ApiParam(value = "Identifier of the corresponsing trading partner ID") @RequestParam(value = "tradingPartnerIDs", required = false) List<String> tradingPartnerIDs,
                                                                                 @ApiParam(value = "Initiation date range for the first process instance in the group") @RequestParam(value = "initiationDateRange", required = false) String initiationDateRange,
                                                                                 @ApiParam(value = "Last activity date range. It is the latest submission date of the document to last process instance in the group") @RequestParam(value = "lastActivityDateRange", required = false) String lastActivityDateRange,
                                                                                 @ApiParam(value = "Offset of the first result among the complete result set satisfying the given criteria", defaultValue = "0") @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                                                                                 @ApiParam(value = "Number of results to be included in the result set", defaultValue = "10") @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
                                                                                 @ApiParam(value = "", defaultValue = "false") @RequestParam(value = "archived", required = false, defaultValue = "false") Boolean archived,
                                                                                 @ApiParam(value = "") @RequestParam(value = "collaborationRole", required = false) String collaborationRole) {
        logger.debug("Getting ProcessInstanceGroups for party: {}", partyID);

        List<ProcessInstanceGroupDAO> processInstanceGroupDAOs = ProcessInstanceGroupDAOUtility.getProcessInstanceGroupDAOs(partyID, collaborationRole, archived, tradingPartnerIDs, relatedProducts, relatedProductCategories, null, null, limit, offset);
        int totalSize = ProcessInstanceGroupDAOUtility.getProcessInstanceGroupSize(partyID, collaborationRole, archived, tradingPartnerIDs, relatedProducts, relatedProductCategories, null, null);
        logger.debug(" There are {} process instance groups in total", processInstanceGroupDAOs.size());
        List<ProcessInstanceGroup> processInstanceGroups = new ArrayList<>();
        for (ProcessInstanceGroupDAO processInstanceGroupDAO : processInstanceGroupDAOs) {

            // In order to apply filter first decompose the elements...
            /*if (partyID != null) {
                logger.debug("Party ID is not null: {}", partyID);
                String firstProcessInstanceID = processInstanceGroupDAO.getProcessInstanceIDs().get(0);
                String lastProcessInstanceID = processInstanceGroupDAO.getProcessInstanceIDs().get(processInstanceGroupDAO.getProcessInstanceIDs().size() - 1);

                ProcessInstanceDAO firstProcessInstance = DAOUtility.getProcessIntanceDAOByID(firstProcessInstanceID);
                String creationDate = firstProcessInstance.getCreationDate();
                logger.debug(" First instance {}, creation date {}...", firstProcessInstanceID, creationDate);
                DateTime creationDateTime = DateUtility.convert(creationDate);

                // get the documents of the last process instance
                List<ProcessDocumentMetadataDAO> lastProcessInstanceDocuments = DAOUtility.getProcessDocumentMetadataByProcessInstanceID(lastProcessInstanceID);
                // give a seed :)
                DateTime lastActivityDateTime = DateUtility.convert("1979-03-11T09:20:00");
                for (ProcessDocumentMetadataDAO processInstanceDocument : lastProcessInstanceDocuments) {
                    DateTime submissionDateTime = DateUtility.convert(processInstanceDocument.getSubmissionDate());
                    // select the latest document submission time
                    if(submissionDateTime.isAfter(lastActivityDateTime))
                        lastActivityDateTime = submissionDateTime;
                }
                logger.debug(" Last instance {}, activity date {}...", lastProcessInstanceID, DateUtility.convert(lastActivityDateTime));

                boolean creationDateFilterOK = false;
                if (initiationDateRange != null) {
                    String[] parts = initiationDateRange.split("_");
                    DateTime start = DateUtility.convert(parts[0]);
                    DateTime finish = DateUtility.convert(parts[1]);

                    creationDateFilterOK = creationDateTime.isAfter(start) && creationDateTime.isBefore(finish);
                } else
                    creationDateFilterOK = true;

                boolean lastActivityDateFilterOK = false;
                if (lastActivityDateRange != null) {
                    String[] parts = lastActivityDateRange.split("_");
                    DateTime start = DateUtility.convert(parts[0]);
                    DateTime finish = DateUtility.convert(parts[1]);

                    lastActivityDateFilterOK = lastActivityDateTime.isAfter(start) && lastActivityDateTime.isBefore(finish);
                } else
                    lastActivityDateFilterOK = true;

                boolean productsFilterOK = false;
                boolean tradingPartnersFilterOK = false;
                // get process instance documents, to reach the related products in the document...
                List<ProcessDocumentMetadataDAO> processInstanceDocuments = DAOUtility.getProcessDocumentMetadataByProcessInstanceID(firstProcessInstanceID);
                for (ProcessDocumentMetadataDAO processInstanceDocument : processInstanceDocuments) {
                    String initiatorID = processInstanceDocument.getInitiatorID();
                    String responderID = processInstanceDocument.getResponderID();
                    logger.debug(" Document initiator {} and document responder {}...", initiatorID, responderID);
                    if (tradingPartnerIDs != null && !tradingPartnerIDs.isEmpty()) {
                        logger.debug(" Trading partners: {}", tradingPartnerIDs);
                        for (String tradingPartner : tradingPartnerIDs) {
                            if (tradingPartner.equals(initiatorID) || tradingPartner.equals(responderID)) {
                                tradingPartnersFilterOK = true;
                                break;
                            }
                        }
                    } else
                        tradingPartnersFilterOK = true;


                    List<String> products = processInstanceDocument.getRelatedProducts();
                    logger.debug(" Document related products {}...", products);
                    if (relatedProducts != null && !relatedProducts.isEmpty()) {
                        for (String product : products) {
                            if (relatedProducts.contains(product)) {
                                productsFilterOK = true;
                                break;
                            }
                        }
                    } else
                        productsFilterOK = true;
                }

                logger.debug(" $$$ Creation Date Filter: {}, Last Activity Date Filter: {}, Trading Partner Filter: {}, Products Filter: {}...",
                        creationDateFilterOK, lastActivityDateFilterOK, tradingPartnersFilterOK, productsFilterOK);
                if (creationDateFilterOK && lastActivityDateFilterOK && tradingPartnersFilterOK && productsFilterOK)
                    processInstanceGroups.add(HibernateSwaggerObjectMapper.createProcessInstanceGroup(processInstanceGroupDAO));
            } else {*/
            processInstanceGroups.add(HibernateSwaggerObjectMapper.createProcessInstanceGroup(processInstanceGroupDAO));
            //}
        }

        ProcessInstanceGroupResponse groupResponse = new ProcessInstanceGroupResponse();
        groupResponse.setProcessInstanceGroups(processInstanceGroups);
        groupResponse.setSize(totalSize);

        ResponseEntity response = ResponseEntity.status(HttpStatus.OK).body(groupResponse);
        logger.debug("Retrieved ProcessInstanceGroups for party: {}", partyID);
        return response;
    }

    @Override
    public ResponseEntity<ProcessInstanceGroupFilter> getProcessInstanceGroupFilters(
            @ApiParam(value = "Identifier of the party") @RequestParam(value = "partyID", required = false) String partyID,
            @ApiParam(value = "Related products") @RequestParam(value = "relatedProducts", required = false) List<String> relatedProducts,
            @ApiParam(value = "Related product categories") @RequestParam(value = "relatedProductCategories", required = false) List<String> relatedProductCategories,
            @ApiParam(value = "Identifier of the corresponsing trading partner ID") @RequestParam(value = "tradingPartnerIDs", required = false) List<String> tradingPartnerIDs,
            @ApiParam(value = "Initiation date range for the first process instance in the group") @RequestParam(value = "initiationDateRange", required = false) String initiationDateRange,
            @ApiParam(value = "Last activity date range. It is the latest submission date of the document to last process instance in the group") @RequestParam(value = "lastActivityDateRange", required = false) String lastActivityDateRange,
            @ApiParam(value = "", defaultValue = "false") @RequestParam(value = "archived", required = false, defaultValue = "false") Boolean archived,
            @ApiParam(value = "") @RequestParam(value = "collaborationRole", required = false) String collaborationRole) {

        ProcessInstanceGroupFilter filters = ProcessInstanceGroupDAOUtility.getFilterDetails(partyID, collaborationRole, archived, tradingPartnerIDs, relatedProducts, relatedProductCategories, null, null);
        ResponseEntity response = ResponseEntity.status(HttpStatus.OK).body(filters);
        logger.debug("Filters retrieved for partyId: {}, archived: {}, products: {}, categories: {}, parties: {}", partyID, archived,
                relatedProducts != null ? relatedProducts.toString() : "[]",
                relatedProductCategories != null ? relatedProductCategories.toString() : "[]",
                tradingPartnerIDs != null ? tradingPartnerIDs.toString() : "[]");
        return response;
    }

    @Override
    public ResponseEntity<ProcessInstanceGroup> archiveGroup(@ApiParam(value = "Identifier of the process instance group to be archived", required = true) @PathVariable("ID") String ID) {
        logger.debug("Archiving ProcessInstanceGroup: {}", ID);

        ProcessInstanceGroupDAO processInstanceGroupDAO = DAOUtility.getProcessInstanceGroupDAO(ID);
        processInstanceGroupDAO.setArchived(true);

        HibernateUtilityRef.getInstance("bp-data-model").update(processInstanceGroupDAO);

        ProcessInstanceGroup processInstanceGroup = HibernateSwaggerObjectMapper.createProcessInstanceGroup(processInstanceGroupDAO);
        ResponseEntity response = ResponseEntity.status(HttpStatus.OK).body(processInstanceGroup);
        logger.debug("Archived ProcessInstanceGroup: {}", ID);
        return response;
    }

    @Override
    public ResponseEntity<ProcessInstanceGroup> restoreGroup(@ApiParam(value = "Identifier of the process instance group to be restored", required = true) @PathVariable("ID") String ID) {
        logger.debug("Restoring ProcessInstanceGroup: {}", ID);

        ProcessInstanceGroupDAO processInstanceGroupDAO = DAOUtility.getProcessInstanceGroupDAO(ID);
        processInstanceGroupDAO.setArchived(false);

        HibernateUtilityRef.getInstance("bp-data-model").update(processInstanceGroupDAO);

        ProcessInstanceGroup processInstanceGroup = HibernateSwaggerObjectMapper.createProcessInstanceGroup(processInstanceGroupDAO);
        ResponseEntity response = ResponseEntity.status(HttpStatus.OK).body(processInstanceGroup);
        logger.debug("Restored ProcessInstanceGroup: {}", ID);
        return response;
    }

    @Override
    public ResponseEntity<Void> saveProcessInstanceGroup(@ApiParam(value = "The content of the process instance group to be saved", required = true) @RequestBody ProcessInstanceGroup processInstanceGroup) {
        logger.debug("Saving ProcessInstanceGroup {}", processInstanceGroup.toString());
        ProcessInstanceGroupDAO processInstanceGroupDAO = HibernateSwaggerObjectMapper.createProcessInstanceGroup_DAO(processInstanceGroup);
        HibernateUtilityRef.getInstance("bp-data-model").persist(processInstanceGroupDAO);

        ResponseEntity response = ResponseEntity.status(HttpStatus.OK).body("true");
        logger.debug("Saved ProcessInstanceGroup {}", processInstanceGroup.toString());
        return response;
    }

    @Override
    public ResponseEntity<Void> archiveAllGroups(@ApiParam(value = "Identifier of the party of which groups will be archived", required = true) @RequestParam(value = "partyID", required = true) String partyID) {
        logger.debug("Archiving ProcessInstanceGroups for party {}", partyID);

        DAOUtility.archiveAllGroupsForParty(partyID);

        ResponseEntity response = ResponseEntity.status(HttpStatus.OK).body("true");
        logger.debug("Archived ProcessInstanceGroup for party {}", partyID);
        return response;
    }

    @Override
    public ResponseEntity<Void> deleteAllArchivedGroups(@ApiParam(value = "Identifier of the party of which groups will be deleted", required = true) @RequestParam(value = "partyID", required = true) String partyID) {
        logger.debug("Deleting archived ProcessInstanceGroups for party {}", partyID);

        DAOUtility.deleteArchivedGroupsForParty(partyID);

        ResponseEntity response = ResponseEntity.status(HttpStatus.OK).body("true");
        logger.debug("Deleted archived ProcessInstanceGroups for party {}", partyID);
        return response;
    }
}
