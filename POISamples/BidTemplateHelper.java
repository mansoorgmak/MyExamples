
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.terracotta.agent.repkg.de.schlichtherle.io.FileInputStream;

import com.fedbid.core.common.DynamicFilePropertiesManager;
import com.fedbid.core.constants.CommonSystemPropertiesConstants;
import com.fedbid.core.constants.FBConstants;
import com.fedbid.core.util.DateUtil;
import com.fedbid.core.util.FedBidFileException;
import com.fedbid.core.util.FedBidLog;
import com.fedbid.core.util.FedBidLogFactory;
import com.fedbid.core.util.FileObject;
import com.fedbid.core.util.StringUtil;
import com.fedbid.ump.constants.BidTemplateModelConstants;
import com.fedbid.ump.constants.CoreModelConstants;
import com.fedbid.ump.constants.PropertyFileKeyConstants;
import com.fedbid.ump.dao.workflow.FileStoreWFDAO;
import com.fedbid.ump.model.dto.bid.BidTemplateFileTO;
import com.fedbid.ump.model.dto.bid.BidTemplateTO;
import com.fedbid.ump.model.response.bid.ImportBidTemplateResponse;
import com.fedbid.ump.model.vo.AttachmentTypeLuVO;
import com.fedbid.ump.model.vo.BidLidVO;
import com.fedbid.ump.model.vo.BidVO;
import com.fedbid.ump.model.vo.BuyLidVO;
import com.fedbid.ump.model.vo.BuyVO;
import com.fedbid.ump.model.vo.DraftBidLidVO;
import com.fedbid.ump.model.vo.DraftBidVO;
import com.fedbid.ump.model.vo.DraftSubBidVO;
import com.fedbid.ump.model.vo.EntityTypeVO;
import com.fedbid.ump.model.vo.FileInfoVO;
import com.fedbid.ump.model.vo.SubBidVO;
import com.fedbid.ump.model.vo.SubBuyVO;
import com.fedbid.ump.model.vo.TemplateMetaDataVO;

public class BidTemplateHelper {
	private static final FedBidLog log = FedBidLogFactory.getInstance(BidTemplateHelper.class);
	
	/*
	 *  Avoid instance variables in multi threaded environment to avoid concurrency issues
	 *  rowCounter is declared as instance variable as the primitive variables or not pass by reference
	 *  If this does not work use a counter inside List or Map to make it pass by reference
	 */
	//private int rowCounter = 0;
		
	public static int COLUMN_WIDTH_MULTIPLIER = 256;
	//public static String DOC_LOCATION="E:/SVN_AppCode/Java-Excel/document";
	public static String FONT_NAME = "Tahoma";
	public static String LINE_ITEMS_BASE_DATES = "Line Items - Base Dates";
	public static String LINE_ITEMS_OPTION_YEAR_DATES = "Line Items - Option Years - Dates";
	public static String LINE_ITEMS_BASE = "Line Items - Base";
	public static String LINE_ITEMS_OPTION_YEARS = "Line Items - Option Years";
	public static String FED_BID = "FedBid";
	public static String BUY_NUMBER = "Buy Number:";
	public static String BID_FILES_FOLDER = "Bid_Files";
	public static String TEMPLATE_FOLDER = "Template";
	public static String BIDS_FOLDER = "Bids";
	public static String TEMPLATE_EXTENSSION = "_Bid_Template.xlsx";
	
	public static String FEDBID_STYLE = "fedbid_style";
	public static String BUY_NUMBER_STYLE = "buy_number_style";
	public static String INSTRUCTION_STYLE = "instructions_style";
	public static String INSTRUCTION_HEADING_STYLE = "instructions_heading_style";
	public static String COMMON_FONT_STYLE = "common_font_style";
	public static String SUB_HEADER_STYLE = "sub_header_style";
	public static String HEADER_COLUMN_STYLE = "header_column_style";
	public static String COLUMN_STYLE = "column_style";
	public static String NO_COLOR_COLUMN_STYLE = "no_color_column_style";
	public static String VALID_INCL_LINE_ITEM = "Included in line item field must be a number greater than zero.";
	public static String VALID_UNIT_PRICE = " cannot be empty and must be greater than zero, unless included in another line item.";
	public static String VALID_REBID_MINIMUM = "Rebid Minimum must be greater than zero, unless included in another line item.";
	public static String SELLER_DESC_LENGTH_ERROR = "Description has exceeded the maximum limit of ";
	public static String MF_LENGTH_ERROR = "Manufacturer has exceeded the maximum limit of ";
	public static String PN_LENGTH_ERROR = "Part Number has exceeded the maximum limit of ";
	public static String PATH_SEPARATOR = "/";
	
	public static String PART_NUMBER = "PART_NUMBER";
	public static String MANUFACTURER = "MANUFACTURER";
	public static String SELLER_DETAILS = "SELLER_DETAILS";
	public static String UNIT_PRICE = "UNIT_PRICE";
	public static String STOP_AUTO_REBID_AT = "STOP_AUTO_REBID_AT";
	public static String INCLUDED_IN_LINE_ITEM = "INCLUDED_IN_LINE_ITEM";
	public static String LINE_ITEM_PRICE = "LINE_ITEM_PRICE";
	public static String CONTRACT_CEILING_PRICE_INITIAL_UNIT_PRICE = "CONTRACT_CEILING_PRICE_INITIAL_UNIT_PRICE";
	
	public Map createBidTemplate(BuyVO buyVO, FileStoreWFDAO fileStoreWFDAO) throws Exception{	
		Map templateMap = new HashMap();
		FileInfoVO fileInfoVO = new FileInfoVO();
		List<TemplateMetaDataVO> metaDataList = new ArrayList();
		
		templateMap.put(BidTemplateModelConstants.FILE_INFO, fileInfoVO);
		templateMap.put(BidTemplateModelConstants.TEMPLATE_POSITIONS, metaDataList);
		
		
		List<Integer> rowCounterList = new ArrayList<Integer>(1);
		rowCounterList.add(new Integer(0));
		
		int rowCounter = rowCounterList.get(0);
		
		long beginTime = System.currentTimeMillis();
		//Create workbook
				
		// Streaming API
		//Workbook workbook = new SXSSFWorkbook(-1);
		
		//Workbook for XSSF
		XSSFWorkbook workbook = new XSSFWorkbook();
				
		// Create styles map for the sheet
		Map<String, CellStyle> styles = createStyles(workbook);
		
		// Create Sheet for streaming API
		//Sheet sheet = workbook.createSheet(buyVO.getBuyNumber()+"-Bid Template");
		
		//Create sheet for XSSF
		XSSFSheet sheet = workbook.createSheet(buyVO.getBuyNumber()+"-Bid Template");
		
		
		// set sheet common params
		sheet = setSheetCommonParams(sheet);
		
        // set Sheet headings
		sheet = setHeadingAndInstructions(styles, sheet, buyVO, rowCounterList, metaDataList);  
		// Get the updated counter
		rowCounter = rowCounterList.get(0);
		
		int counter = 0;
		Set<SubBuyVO> subBuySet = buyVO.getSubBuySet();
		Iterator<SubBuyVO> iterator = subBuySet.iterator();
		
		boolean includedInLineItemFlag = false;
		while(iterator.hasNext()){
			SubBuyVO subBuyVO = iterator.next();
			if(subBuyVO.getAuctionLidSet() != null && subBuyVO.getAuctionLidSet().size() > 1){
				includedInLineItemFlag = true;
				break;
			}
		}
		
		iterator = subBuySet.iterator();
		while(iterator.hasNext()){
			SubBuyVO subBuyVO = iterator.next();
			boolean multileLineItemsFlag = false;
			if(subBuyVO.getAuctionLidSet() != null && subBuyVO.getAuctionLidSet().size() > 1){
				multileLineItemsFlag = true;
			}
			// Get list of columns to display in the template		
			List<BidTemplateModelConstants.COLUMNS_ENUM> columnList = getTemplateColumns(buyVO, includedInLineItemFlag);
			
			// set Column sizes for column starting from B in the sheet		
			setColumnSize(sheet, columnList);
			
			rowCounter++;
			Row row = sheet.createRow(rowCounter);
			row.setHeightInPoints(20);
			String rangeAddress = "$"+getCellType(1)+"$"+(rowCounter+1)+":$"+getCellType(columnList.size()+1)+"$"+(rowCounter+1)+""; //"$B$11:$K$11"
	        sheet.addMergedRegion(CellRangeAddress.valueOf(rangeAddress)); 
	        for(int k=1; k<=columnList.size(); k++){
	        	row.createCell(k).setCellStyle(styles.get(SUB_HEADER_STYLE));
	        }        
	        Cell cell = row.getCell(1);
	        
	        if(buyVO.getIsOptionBuy() > 0){
	        	cell.setCellValue(BidTemplateModelConstants.COLUMNS_ENUM.LINE_ITEMS.getColumnName()+" "+subBuyVO.getSubBuyNumber() + "     ("+DateUtil.getStrDate(subBuyVO.getStartDate())+" - "+DateUtil.getStrDate(subBuyVO.getEndDate())+")");
			}else{
				cell.setCellValue(BidTemplateModelConstants.COLUMNS_ENUM.LINE_ITEMS.getColumnName());
			}
	        
	        if(counter == 0){ // Header columns are required only once for the base option for an option buy or non option buy
		        rowCounter++;		        
				row = sheet.createRow(rowCounter);		
				row.setHeightInPoints(40);
				
				// Add row starting for the base option
				addTemplateMetaData(metaDataList, 
											buyVO.getBuyId(), 
											subBuyVO.getSubBuyPK().getSubBuyId(), 
											BidTemplateModelConstants.COLUMNS_ENUM.LINE_ITEMS.getEntityName()+"_"+subBuyVO.getSubBuyNumber(), 
											(rowCounter+1),1, 
											BidTemplateModelConstants.ENTITY_TYPE_ROW);
				
				// 
				for(int l=1; l<=columnList.size(); l++){
					BidTemplateModelConstants.COLUMNS_ENUM columnName = columnList.get(l-1);
					Cell headerColumnCell = row.createCell(l);
					headerColumnCell.setCellValue(columnName.getColumnName());
					headerColumnCell.setCellStyle(styles.get(HEADER_COLUMN_STYLE));
					
					// Add column meta data for the base option
					if(!columnName.getEntityName().equals(BidTemplateModelConstants.COLUMNS_ENUM.INCLUDED_IN_LINE_ITEM.getEntityName()) ||
							(columnName.getEntityName().equals(BidTemplateModelConstants.COLUMNS_ENUM.INCLUDED_IN_LINE_ITEM.getEntityName()) && multileLineItemsFlag)){
						addTemplateMetaData(metaDataList, 
								buyVO.getBuyId(), 
								subBuyVO.getSubBuyPK().getSubBuyId(), 
								columnName.getEntityName(), 
								(rowCounter+1), l, 
								BidTemplateModelConstants.ENTITY_TYPE_COLUMN);
					}
				}
	        }else{
	        	// Add row starting for the remaining options 
	        	addTemplateMetaData(metaDataList, 
	        									buyVO.getBuyId(), 
	        									subBuyVO.getSubBuyPK().getSubBuyId(), 
	        									BidTemplateModelConstants.COLUMNS_ENUM.LINE_ITEMS.getEntityName()+"_"+subBuyVO.getSubBuyNumber(), 
	        									(rowCounter+1), 1, 
	        									BidTemplateModelConstants.ENTITY_TYPE_ROW);
	        	
	        	for(int l=1; l<=columnList.size(); l++){
					BidTemplateModelConstants.COLUMNS_ENUM columnName = columnList.get(l-1);
					
					// Add columns for the remaining options
					if(!columnName.getEntityName().equals(BidTemplateModelConstants.COLUMNS_ENUM.INCLUDED_IN_LINE_ITEM.getEntityName()) ||
							(columnName.getEntityName().equals(BidTemplateModelConstants.COLUMNS_ENUM.INCLUDED_IN_LINE_ITEM.getEntityName()) && multileLineItemsFlag)){
						addTemplateMetaData(metaDataList, 
								buyVO.getBuyId(), 
								subBuyVO.getSubBuyPK().getSubBuyId(), 
								columnName.getEntityName(), 
								(rowCounter+1), l, 
								BidTemplateModelConstants.ENTITY_TYPE_COLUMN);
					}
				}
	        }
			        	
			Set<BuyLidVO> buyLidSet = subBuyVO.getAuctionLidSet();	
			Iterator<BuyLidVO> buyLidIterator = buyLidSet.iterator();			
			while(buyLidIterator.hasNext()){
				BuyLidVO buyLidVO = buyLidIterator.next();
				rowCounter++;
				row = sheet.createRow(rowCounter);
				// UnComment below line if Text Wrapping is not required
				//row.setHeightInPoints(20);
				
				// If the sub buy has more than one line item then allow blank cell for included in line item otherwise disable cell
				// Make sure that the Included in line item column is the last column
				
				int columnListSize = columnList.size();
				if(includedInLineItemFlag && !multileLineItemsFlag){
					columnListSize = columnListSize-1;
				}
				
				for(int m=1; m<=columnListSize; m++){					
					Cell blankCell = row.createCell(m);
					blankCell.setCellType(Cell.CELL_TYPE_STRING);
					if(m == 1){
						blankCell.setCellStyle(styles.get(COLUMN_STYLE));
						blankCell.setCellValue(buyLidVO.getItemId());
					}else if(m == 2){
						blankCell.setCellStyle(styles.get(COLUMN_STYLE));
						blankCell.setCellValue(buyLidVO.getCriteria());
					}else if(m == 3){
						blankCell.setCellStyle(styles.get(COLUMN_STYLE));
						blankCell.setCellValue(buyLidVO.getQuantity());
					}else if(m == 4){
						blankCell.setCellStyle(styles.get(COLUMN_STYLE));
						blankCell.setCellValue(buyLidVO.getMeasurement());
					}else{						
						blankCell.setCellStyle(styles.get(NO_COLOR_COLUMN_STYLE));												
						addColumnValidation(rowCounter, m, columnList.get(m-1), sheet);																								
					}					
				}
		
			}
				        
	        counter ++;
		}
		
		/*
		rowCounter++;
		Row row = sheet.createRow(rowCounter);
		Cell cell = row.createCell(1);
		cell.setCellValue("");
		
		try{			
			String bidTemplateDesclaimer = DynamicFilePropertiesManager.
					getPropertyConfig(PropertyFileKeyConstants.COMMON_SYSTEM_PROPERTIES).
						getString(CommonSystemPropertiesConstants.BID_TEMPLATE_DESCLAIMER, "");
	    	bidTemplateDesclaimer = StringUtil.unNuller(bidTemplateDesclaimer);
	    	StringTokenizer desclaimerTokenizer = new StringTokenizer(bidTemplateDesclaimer, "~");
	    	while(desclaimerTokenizer.hasMoreTokens()){
	    		String desclaimer = desclaimerTokenizer.nextToken();
	    		// Disclaimer Text
				rowCounter++;
				row = sheet.createRow(rowCounter);
				cell = row.createCell(1);
				cell.setCellStyle(styles.get(INSTRUCTION_HEADING_STYLE));
				cell.setCellValue(desclaimer);
	    	}			
		}catch(Exception exp){
			log.error("Error while reading bid template desclaimer", exp);
		}
		*/
		
		//Update row counter list
		rowCounterList.add(0, rowCounter);
		
		// Write work book to the S3
        writeWorkbook(workbook, buyVO.getBuyNumber(), fileStoreWFDAO, fileInfoVO);
        
        long endTime = System.currentTimeMillis();
        
        workbook.close();
        workbook = null;
        
        log.info(" Total time taken to create the bid template == "+(endTime-beginTime));        
        
        return templateMap;
	}
	
		
	private void setColumnSize(Sheet sheet, List<BidTemplateModelConstants.COLUMNS_ENUM> columnList){
		for(int k=0; k<columnList.size(); k++){
			BidTemplateModelConstants.COLUMNS_ENUM column = columnList.get(k);
			sheet.setColumnWidth(k+1, column.getColumnWidth()*COLUMN_WIDTH_MULTIPLIER);
		}
			
	}
	
	private List<BidTemplateModelConstants.COLUMNS_ENUM> getTemplateColumns(BuyVO buyVO, boolean includedInLineItem){
		List<BidTemplateModelConstants.COLUMNS_ENUM> columnList = new ArrayList<BidTemplateModelConstants.COLUMNS_ENUM>();
		
		columnList.add(BidTemplateModelConstants.COLUMNS_ENUM.BUY_ITEM_NO);
		columnList.add(BidTemplateModelConstants.COLUMNS_ENUM.BUY_DESCRIPTION);
		columnList.add(BidTemplateModelConstants.COLUMNS_ENUM.QUANTITY);
		columnList.add(BidTemplateModelConstants.COLUMNS_ENUM.UNIT);
		
		try{
    		boolean displayBidLidDetails = DynamicFilePropertiesManager.
    											getPropertyConfig(PropertyFileKeyConstants.COMMON_SYSTEM_PROPERTIES).
    												getBoolean(CommonSystemPropertiesConstants.DISPLAY_BID_LID_DETAILS, false);
    		
    	// If not exact match
		if(buyVO.getEvaluationCriteriaId() != CoreModelConstants.BID_EVAL_CRITERIA_EXACT_MATCH){
			// part number and manufacturer display is enabled
			if(buyVO.getEvaluationCriteriaId() != CoreModelConstants.BID_EVAL_CRITERIA_DETERMINE_BY_LINE_ITEM){
				if(displayBidLidDetails == true){		
					columnList.add(BidTemplateModelConstants.COLUMNS_ENUM.MANUFACTURER);
					columnList.add(BidTemplateModelConstants.COLUMNS_ENUM.PART_NUMBER);					
				}
			}
			
			columnList.add(BidTemplateModelConstants.COLUMNS_ENUM.SELLER_DESCRIPTION);
		}
				
		}catch(Exception exp){
    		log.error("Error while loading dis_bid_lid_deatils property", exp);
    	}
		
		if(buyVO.getContractTypeId() == CoreModelConstants.OPEN_MARKET_CONTRACT_TYPE_ID){
			if(buyVO.getDecrementTypeId() == CoreModelConstants.DECREMENT_ID_BAFO){
				columnList.add(BidTemplateModelConstants.COLUMNS_ENUM.LINE_ITEM_PRICE);
			}else{
				columnList.add(BidTemplateModelConstants.COLUMNS_ENUM.UNIT_PRICE);
			}
			
		}else if(buyVO.getContractTypeId() != CoreModelConstants.OPEN_MARKET_CONTRACT_TYPE_ID){
			columnList.add(BidTemplateModelConstants.COLUMNS_ENUM.CONTRACT_PRICE);
		}
		
		if(buyVO.getDecrementTypeId() != CoreModelConstants.DECREMENT_ID_BAFO){
			columnList.add(BidTemplateModelConstants.COLUMNS_ENUM.STOP_AUTO_REBID_AT);
		}
		
		
		/* Always make sure that the Included in line item column is the last 
		 * column in the sheet, otherwise included in column disabling will fail 
		 * when there is only one line item in the sub buy
		 */
		if(includedInLineItem){
			columnList.add(BidTemplateModelConstants.COLUMNS_ENUM.INCLUDED_IN_LINE_ITEM);
		}
		
		return columnList;
	}
	
	private XSSFSheet setSheetCommonParams(XSSFSheet sheet){
		// disable row and column headings
		sheet.setDisplayRowColHeadings(false);
		// turn off gridlines
        sheet.setDisplayGridlines(false);
        // turn off gridline during printing
        sheet.setPrintGridlines(false);
        sheet.setFitToPage(true);
        sheet.setHorizontallyCenter(true);
        PrintSetup printSetup = sheet.getPrintSetup();
        // set print layout to landscape
        printSetup.setLandscape(true);
        
        //the following three statements are required only for HSSF
        sheet.setAutobreaks(true);
        printSetup.setFitHeight((short)1);
        printSetup.setFitWidth((short)1);
        
        /************ Added excel locking feature ************/        
        //Lock sheet
        sheet.enableLocking();
        //disable rows insertion/deletion
        sheet.lockInsertColumns(true);
        sheet.lockDeleteRows(true);
        
        //disable columns insertion/deletion
        sheet.lockInsertColumns(true);
        sheet.lockDeleteColumns(true);        
        /*****************************************************/
        
        // Set first column width to 1/4th
        sheet.setColumnWidth(0, COLUMN_WIDTH_MULTIPLIER*2);
                
        return sheet;
	}
	
	private XSSFSheet setHeadingAndInstructions(Map<String, CellStyle> styles, XSSFSheet sheet, BuyVO buyVO, List<Integer> rowCounterList, List<TemplateMetaDataVO> metaDataList){
		try{
			
	    	String bidTemplateInstructions = DynamicFilePropertiesManager.
	    											getPropertyConfig(PropertyFileKeyConstants.COMMON_SYSTEM_PROPERTIES).
	    												getString(CommonSystemPropertiesConstants.BID_TEMPLATE_INSTRUCTIONS, "");
	    	bidTemplateInstructions = StringUtil.unNuller(bidTemplateInstructions);
	    	
	    	String bidTemplateNote = DynamicFilePropertiesManager.
					getPropertyConfig(PropertyFileKeyConstants.COMMON_SYSTEM_PROPERTIES).
						getString(CommonSystemPropertiesConstants.BID_TEMPLATE_NOTE, "");
	    	bidTemplateNote = StringUtil.unNuller(bidTemplateNote);

	    	// get the updated counter
			int rowCounter = rowCounterList.get(0);
			// Set FedBid symbol        
	        Row row = sheet.createRow(rowCounter);
	        row.setHeightInPoints(40);
	             
	        Cell cell = row.createCell(1);
	        cell.setCellValue(FED_BID);
	        cell.setCellStyle(styles.get(FEDBID_STYLE));
	        
	        // set Buy Number
	        rowCounter++;
	        row = sheet.createRow(rowCounter);
	        cell = row.createCell(1);
	        cell.setCellStyle(styles.get(BUY_NUMBER_STYLE));
	        cell.setCellValue(BUY_NUMBER+" "+buyVO.getBuyNumber());
	        addTemplateMetaData(metaDataList, buyVO.getBuyId(), CoreModelConstants.BASE_SUB_BUY_ID, BidTemplateModelConstants.COLUMNS_ENUM.BUY_NUMBER.getEntityName(), rowCounter,1, BidTemplateModelConstants.ENTITY_TYPE_ROW);
	        
	        rowCounter++;
	        row = sheet.createRow(rowCounter);
	        cell = row.createCell(1);        
	        cell.setCellValue("");
	        
	        // set instructions
	        StringTokenizer instructionsTokenizer = new StringTokenizer(bidTemplateInstructions, "~");
	        while(instructionsTokenizer.hasMoreTokens()){
	        	String instruction = instructionsTokenizer.nextToken();
	        	rowCounter++;
		        row = sheet.createRow(rowCounter);
		        cell = row.createCell(1);
		        cell.setCellStyle(styles.get(INSTRUCTION_STYLE));
		        cell.setCellValue(instruction);
	        }	        
	        
	        rowCounter++;
	        row = sheet.createRow(rowCounter);
	        cell = row.createCell(1);
	        cell.setCellValue("");
	        
		    // set bid template notes
	        StringTokenizer noteTokenizer = new StringTokenizer(bidTemplateNote, "~");
	        while(noteTokenizer.hasMoreTokens()){
	        	String note = noteTokenizer.nextToken();
	        	rowCounter++;
		        row = sheet.createRow(rowCounter);
		        cell = row.createCell(1);
		        cell.setCellStyle(styles.get(INSTRUCTION_STYLE));
		        cell.setCellValue(note);
	        }	     
	        
	        rowCounter++;
	        row = sheet.createRow(rowCounter);
	        cell = row.createCell(1);
	        cell.setCellValue("");
	        
	        // Update row counter in List
	        rowCounterList.add(0,rowCounter);
	        	        
		}catch(Exception exp){
			log.error("Error while adding the bid template heading", exp);
		}
		
		return sheet;
	}
	
	
	private void readFile(String buyNumber) throws Exception{
		long beginTime = System.currentTimeMillis();
		String fileName = buyNumber+TEMPLATE_EXTENSSION;
		File file = new File(fileName);
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		//Workbook workbook = WorkbookFactory.create(file);
		
		Workbook workbook = null;

        if(fileName.equals("-xls")){
        	workbook = new HSSFWorkbook(bis);        
        }else{
        	workbook = new XSSFWorkbook(bis);
        }
        
        
		
		Sheet sheet = workbook.getSheetAt(0);
		if(sheet == null){
			throw new Exception("Invalid Sheet at sheet 0");
		}
		
		Map<String, String> map = new HashMap<String, String>();
		for(int k=12; k<2012; k++){
			Row row = sheet.getRow(k);
			
			try{
				//System.out.println(row.getCell(1).getNumericCellValue()+""+"------------------------"+ row.getCell(2).getStringCellValue());
				map.put(row.getCell(1).getNumericCellValue()+"", row.getCell(2).getStringCellValue());
			}catch(Exception exp){
				System.out.println("Error while reading the rows/cell ==== "+exp.getMessage());
			}
		}
		
		long endTime = System.currentTimeMillis();
		
		System.out.println(" Time taken to read file ------== "+(endTime-beginTime));
		
		workbook.close();
		workbook = null;
		bis.close();
		bis = null;
	}
	
	
	private void writeWorkbook(Workbook workbook, String buyNumber, FileStoreWFDAO fileStoreWFDAO, FileInfoVO fileInfoVO) throws Exception{
		// Write the output to a file
               
        //if(workbook instanceof XSSFWorkbook) file += "x";        
        //FileOutputStream out = new FileOutputStream(file);
		
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        workbook.write(baos);
        //out.write(baos.toByteArray());    
                        
        Collection<FileObject> files = getFileObject(buyNumber, baos.toByteArray(), fileInfoVO);
        fileStoreWFDAO.createFile(files);
        
        log.info("--------------"+baos.size() +"-----------"+ baos.toByteArray().length+"--------"+baos.toByteArray());
        //out.close();
        baos.close();
        baos = null;
        log.debug("Bid template for buy "+buyNumber+" is created");
	}
	
	private Collection<FileObject> getFileObject(String buyNumber, byte[] bytes, FileInfoVO fileInfoVO){
		Collection<FileObject> files = new ArrayList();
		FileObject fileObject = new FileObject();
		fileObject.setFileName(buyNumber+TEMPLATE_EXTENSSION);
		fileObject.setDisplayFileName(buyNumber+TEMPLATE_EXTENSSION);
		fileObject.setBytes(bytes);
		List<String> pathList = new ArrayList();
		pathList.add(BID_FILES_FOLDER);
		pathList.add(buyNumber);
		pathList.add(TEMPLATE_FOLDER);
		fileObject.setPath(pathList);
		files.add(fileObject);
		
		// Create FileInfoVO object
		AttachmentTypeLuVO attachmentType = new AttachmentTypeLuVO();
		attachmentType.setAttachmentTypeId(CoreModelConstants.BID_TEMPLATE_ATTACHMENT_TYPE);
		fileInfoVO.setAttachmentType(attachmentType);
		fileInfoVO.setDateCreated(new Date());
		fileInfoVO.setFileName(buyNumber+TEMPLATE_EXTENSSION);
		fileInfoVO.setFilePath(BID_FILES_FOLDER+PATH_SEPARATOR+buyNumber+PATH_SEPARATOR+TEMPLATE_FOLDER);
		
		return files;
	}
	
		
	public String storeImportBidFile(String buyNumber, Integer bidId, Integer fileId, BidTemplateFileTO templateFileTO, FileStoreWFDAO fileStoreWFDAO){		
		Collection<FileObject> files = new ArrayList();
		FileObject fileObject = new FileObject();
		fileObject.setFileName(templateFileTO.getFileName());
		fileObject.setDisplayFileName(templateFileTO.getFileName());
		fileObject.setBytes(templateFileTO.getContent());
		List<String> pathList = new ArrayList();
		pathList.add(BID_FILES_FOLDER);
		pathList.add(buyNumber);
		pathList.add(BIDS_FOLDER);
		pathList.add(bidId.intValue()+"");
		pathList.add(fileId.intValue()+"");
		fileObject.setPath(pathList);
		files.add(fileObject);
		try{			
			fileStoreWFDAO.createFile(files);		
			return BID_FILES_FOLDER+PATH_SEPARATOR+buyNumber+PATH_SEPARATOR+BIDS_FOLDER+PATH_SEPARATOR+bidId.intValue()+PATH_SEPARATOR+fileId.intValue();
		}catch(FedBidFileException exp){
			exp.printStackTrace();
			log.error("Error while storing imported bid template", exp);
		}
		
		return "";
	}
	
	
	/**
     * cell styles used for formatting calendar sheets
     */
    private static Map<String, CellStyle> createStyles(Workbook wb){
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style;     
        
        style = wb.createCellStyle();
        Font fedBidTitleFont = wb.createFont();
        fedBidTitleFont.setBold(true);
        fedBidTitleFont.setFontHeightInPoints((short)16);
        fedBidTitleFont.setFontName(FONT_NAME);
        fedBidTitleFont.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(fedBidTitleFont);
        style.setAlignment(CellStyle.ALIGN_GENERAL);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        styles.put(FEDBID_STYLE, style);
                
        style = wb.createCellStyle();
        Font buyNumberFont = wb.createFont();
        buyNumberFont.setBold(true);
        buyNumberFont.setItalic(true);
        buyNumberFont.setFontHeightInPoints((short)14);
        buyNumberFont.setFontName(FONT_NAME);
        buyNumberFont.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFont(buyNumberFont);
        style.setAlignment(CellStyle.ALIGN_GENERAL);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        styles.put(BUY_NUMBER_STYLE, style);
               
        style = wb.createCellStyle();
        Font instructionsFont = wb.createFont();
        instructionsFont.setFontName(FONT_NAME);
        instructionsFont.setFontHeightInPoints((short)10);
        instructionsFont.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setAlignment(CellStyle.ALIGN_GENERAL);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFont(instructionsFont);
        styles.put(INSTRUCTION_STYLE, style);
        
        style = wb.createCellStyle();
        Font instructionsHeadingFont = wb.createFont();
        instructionsHeadingFont.setFontName(FONT_NAME);
        instructionsHeadingFont.setFontHeightInPoints((short)10);
        instructionsHeadingFont.setBold(true);
        instructionsHeadingFont.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setAlignment(CellStyle.ALIGN_GENERAL);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFont(instructionsHeadingFont);
        styles.put(INSTRUCTION_HEADING_STYLE, style);
        
        style = wb.createCellStyle();
        Font commonFont = wb.createFont();
        commonFont.setFontName(FONT_NAME);
        commonFont.setFontHeightInPoints((short)10);     
        style.setAlignment(CellStyle.ALIGN_GENERAL);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFont(commonFont);
        styles.put(COMMON_FONT_STYLE, style);
                
        CellStyle subHeaderStyle = wb.createCellStyle();
        subHeaderStyle.setBorderLeft(CellStyle.BORDER_THIN);
        subHeaderStyle.setBorderRight(CellStyle.BORDER_THIN);
        subHeaderStyle.setBorderBottom(CellStyle.BORDER_THIN);        
        subHeaderStyle.setBorderTop(CellStyle.BORDER_THIN);
        subHeaderStyle.setAlignment(CellStyle.ALIGN_CENTER);
        subHeaderStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        subHeaderStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        subHeaderStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        Font font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short)12);
        font.setFontName(FONT_NAME);
        font.setColor(IndexedColors.BLACK.getIndex());
        subHeaderStyle.setFont(font);
        styles.put(SUB_HEADER_STYLE, subHeaderStyle);
        
                
        CellStyle headerStyle = wb.createCellStyle();
        headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headerStyle.setBorderRight(CellStyle.BORDER_THIN);
        headerStyle.setBorderBottom(CellStyle.BORDER_THIN);        
        headerStyle.setBorderTop(CellStyle.BORDER_THIN);
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);        
        headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        Font columnHeaderFont = wb.createFont();
        columnHeaderFont.setBold(true);        
        columnHeaderFont.setFontName(FONT_NAME);
        columnHeaderFont.setColor(IndexedColors.BLACK.getIndex());
        columnHeaderFont.setFontHeightInPoints((short)10);
        headerStyle.setWrapText(true);
        headerStyle.setFont(columnHeaderFont);
        styles.put(HEADER_COLUMN_STYLE, headerStyle);
        
        
        CellStyle columnStyle = wb.createCellStyle();
        columnStyle.setBorderLeft(CellStyle.BORDER_THIN);
        columnStyle.setBorderRight(CellStyle.BORDER_THIN);
        columnStyle.setBorderBottom(CellStyle.BORDER_THIN);
        columnStyle.setBorderTop(CellStyle.BORDER_THIN);
        columnStyle.setAlignment(CellStyle.ALIGN_LEFT);
        columnStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        columnStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        columnStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        columnStyle.setWrapText(true);
        columnStyle.setShrinkToFit(true);        
        styles.put(COLUMN_STYLE, columnStyle);	
        
        CellStyle noColorColumnStyle = wb.createCellStyle();
        noColorColumnStyle.setBorderLeft(CellStyle.BORDER_THIN);
        noColorColumnStyle.setBorderRight(CellStyle.BORDER_THIN);
        noColorColumnStyle.setBorderBottom(CellStyle.BORDER_THIN);
        noColorColumnStyle.setBorderTop(CellStyle.BORDER_THIN);        
        noColorColumnStyle.setWrapText(true);
        // Only cells unlocked for editing purporse
        noColorColumnStyle.setLocked(false);
        styles.put(NO_COLOR_COLUMN_STYLE, noColorColumnStyle);
        
        return styles;
    }
    
    private String getCellType(int cellNumber){
    	String cellName = "";
    	switch(cellNumber){
    	case 0:
    		cellName = "A";
    		break;
    	case 1:
    		cellName = "B";
    		break;
    	case 2:
    		cellName = "C";
    		break;
    	case 3:
    		cellName = "D";
    		break;
    	case 4:
    		cellName = "D";
    		break;
    	case 5:
    		cellName = "E";
    		break;
    	case 6:
    		cellName = "F";
    		break;
    	case 7:
    		cellName = "G";
    		break;
    	case 8:
    		cellName = "H";
    		break;
    	case 9:
    		cellName = "I";
    		break;
    	case 10:
    		cellName = "J";
    		break;
    	case 11:
    		cellName = "K";
    		break;
    	case 12:
    		cellName = "L";
    		break;
    	case 13:
    		cellName = "M";
    		break;
    	case 14:
    		cellName = "N";
    		break;
    	case 15:
    		cellName = "O";
    		break;
    	}
    	
    	
    	return cellName;
    }
       
    private void addTemplateMetaData(List<TemplateMetaDataVO> metadataList, Integer buyId, Integer subBuyId, String entityName, int rowStartPosition, int columnPosition, int entityTypeId){
    	TemplateMetaDataVO templateMetaDataVO = new TemplateMetaDataVO();
    	templateMetaDataVO.setBuyId(buyId);
    	templateMetaDataVO.setDateCreated(new Date());
    	templateMetaDataVO.setEntityName(entityName);
    	templateMetaDataVO.setRowStartPosition(rowStartPosition);
    	templateMetaDataVO.setColumnPosition(columnPosition);
    	EntityTypeVO entityType = new EntityTypeVO();
    	entityType.setEntityTypeId(entityTypeId);
    	templateMetaDataVO.setEntityType(entityType);
    	templateMetaDataVO.setStatus(CoreModelConstants.STATUS_ACTIVE);
    	templateMetaDataVO.setSubBuyId(subBuyId);    	 	
    	
    	metadataList.add(templateMetaDataVO);
    }
    
    
    private void addColumnValidation(int rowIndex, 
    									int columnIndex, 
    									BidTemplateModelConstants.COLUMNS_ENUM column,     									
    									XSSFSheet sheet) throws Exception{
    	XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);		
		if(column.getColumnName().equals(BidTemplateModelConstants.COLUMNS_ENUM.LINE_ITEM_PRICE.getColumnName()) ||
				column.getColumnName().equals(BidTemplateModelConstants.COLUMNS_ENUM.CONTRACT_PRICE.getColumnName()) ||
				column.getColumnName().equals(BidTemplateModelConstants.COLUMNS_ENUM.UNIT_PRICE.getColumnName())){
			
			XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)dvHelper.createDecimalConstraint(DataValidationConstraint.OperatorType.GREATER_THAN, "0", null);
	        CellRangeAddressList addressList = new CellRangeAddressList(rowIndex, rowIndex, columnIndex, columnIndex);
	        XSSFDataValidation validation = (XSSFDataValidation)dvHelper.createValidation(dvConstraint, addressList);
	        validation.createErrorBox("validUnitPrice", column.getColumnName()+VALID_UNIT_PRICE);
	        validation.setShowErrorBox(true);
	        sheet.addValidationData(validation);
			
		}else if(column.getColumnName().equals(BidTemplateModelConstants.COLUMNS_ENUM.STOP_AUTO_REBID_AT.getColumnName())){
			XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)dvHelper.createDecimalConstraint(DataValidationConstraint.OperatorType.GREATER_THAN, "0", null);
	        CellRangeAddressList addressList = new CellRangeAddressList(rowIndex, rowIndex, columnIndex, columnIndex);
	        XSSFDataValidation validation = (XSSFDataValidation)dvHelper.createValidation(dvConstraint, addressList);
	        validation.createErrorBox("validRebidMin", VALID_REBID_MINIMUM);
	        validation.setShowErrorBox(true);
	        sheet.addValidationData(validation);
		}else if(column.getColumnName().equals(BidTemplateModelConstants.COLUMNS_ENUM.INCLUDED_IN_LINE_ITEM.getColumnName())){				
			XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)dvHelper.createIntegerConstraint(DataValidationConstraint.OperatorType.GREATER_THAN, "0", null);
	        CellRangeAddressList addressList = new CellRangeAddressList(rowIndex, rowIndex, columnIndex, columnIndex);
	        XSSFDataValidation validation = (XSSFDataValidation)dvHelper.createValidation(dvConstraint, addressList);
	        validation.createErrorBox("validInclLineItem", VALID_INCL_LINE_ITEM);
	        validation.setShowErrorBox(true);
	        sheet.addValidationData(validation);
		}else if(column.getColumnName().equals(BidTemplateModelConstants.COLUMNS_ENUM.SELLER_DESCRIPTION.getColumnName())){
			
			String sellerDescLength = DynamicFilePropertiesManager.
					getPropertyConfig(PropertyFileKeyConstants.COMMON_SYSTEM_PROPERTIES).
						getString(CommonSystemPropertiesConstants.SELLER_DESC_LENGTH, "");
			
			XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
						dvHelper.createNumericConstraint(DataValidationConstraint.ValidationType.TEXT_LENGTH, DataValidationConstraint.OperatorType.LESS_OR_EQUAL, sellerDescLength, null);
	        CellRangeAddressList addressList = new CellRangeAddressList(rowIndex, rowIndex, columnIndex, columnIndex);
	        XSSFDataValidation validation = (XSSFDataValidation)dvHelper.createValidation(dvConstraint, addressList);
	        validation.createErrorBox("SDLength", SELLER_DESC_LENGTH_ERROR+sellerDescLength+" characters on line item");
	        validation.setShowErrorBox(true);
	        sheet.addValidationData(validation);
		}else if(column.getColumnName().equals(BidTemplateModelConstants.COLUMNS_ENUM.MANUFACTURER.getColumnName())){
			
			String manufacturerLength = DynamicFilePropertiesManager.
					getPropertyConfig(PropertyFileKeyConstants.COMMON_SYSTEM_PROPERTIES).
						getString(CommonSystemPropertiesConstants.MANUFACTURER_LENGTH, "");
			
			XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
						dvHelper.createNumericConstraint(DataValidationConstraint.ValidationType.TEXT_LENGTH, DataValidationConstraint.OperatorType.LESS_OR_EQUAL, manufacturerLength, null);
	        CellRangeAddressList addressList = new CellRangeAddressList(rowIndex, rowIndex, columnIndex, columnIndex);
	        XSSFDataValidation validation = (XSSFDataValidation)dvHelper.createValidation(dvConstraint, addressList);
	        validation.createErrorBox("MfLength", MF_LENGTH_ERROR+manufacturerLength+" characters on line item");
	        validation.setShowErrorBox(true);
	        sheet.addValidationData(validation);
		}else if(column.getColumnName().equals(BidTemplateModelConstants.COLUMNS_ENUM.PART_NUMBER.getColumnName())){
			
			String partNumberLength = DynamicFilePropertiesManager.
					getPropertyConfig(PropertyFileKeyConstants.COMMON_SYSTEM_PROPERTIES).
						getString(CommonSystemPropertiesConstants.PART_NUMBER_LENGTH, "");
			
			XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
						dvHelper.createNumericConstraint(DataValidationConstraint.ValidationType.TEXT_LENGTH, DataValidationConstraint.OperatorType.LESS_OR_EQUAL, partNumberLength, null);
	        CellRangeAddressList addressList = new CellRangeAddressList(rowIndex, rowIndex, columnIndex, columnIndex);
	        XSSFDataValidation validation = (XSSFDataValidation)dvHelper.createValidation(dvConstraint, addressList);
	        validation.createErrorBox("MfLength", PN_LENGTH_ERROR+partNumberLength+" characters on line item");
	        validation.setShowErrorBox(true);
	        sheet.addValidationData(validation);
		}
    }
    
    public ImportBidTemplateResponse validateAndSetBidLidData(BuyVO buyVO, Integer bidId, List<TemplateMetaDataVO> metaDataList, BidTemplateFileTO templateFileTO) throws BidTemplateException{
    	log.info("................. Begin in BidTemplateHelper.validateAndSetBidLidData ............");
    	Hashtable<String, String> errors = new Hashtable<String, String>();
    	ImportBidTemplateResponse response = new ImportBidTemplateResponse();
    	response.setMessageMap(errors);
    	
    	Map<Integer, List<BidTemplateTO>> bidLidDataMap = new HashMap();
    	
    	try{    		
    		String fileName = templateFileTO.getFileName();
    		byte[] bidContent = templateFileTO.getContent();
    		
    		log.info(" ............... Get bytes from imported Bid Lid Data for buy "+buyVO.getBuyNumber());
    		
    		ByteArrayInputStream bis = new ByteArrayInputStream(bidContent);
    		Workbook workbook = null;
    		
    		if(metaDataList == null || metaDataList.size() == 0){
    			throw new BidTemplateException("Bid Template Metadata for buy "+buyVO.getBuyNumber()+" not found");
    		}
    		
            if(fileName.endsWith(".xls")){
            	workbook = new HSSFWorkbook(bis);        
            }else if(fileName.endsWith(".xlsx")){
            	workbook = new XSSFWorkbook(bis);
            }else if(fileName.endsWith(".csv")){
            	throw new BidTemplateException("The template was not uploaded because the format was incorrect");
            }else{
            	throw new BidTemplateException("The template was not uploaded because the format was incorrect");
            }
                       
    		
    		Sheet sheet = workbook.getSheetAt(0);
    		if(sheet == null){
    			log.error("Invalid Bid Template, sheet not found");
    			throw new BidTemplateException("The template was not uploaded because the format was incorrect");        	        		
    		}
    		
    		Set<SubBuyVO> subBuySet = buyVO.getSubBuySet();
    		Iterator<SubBuyVO> iterator = subBuySet.iterator();
    		log.info(" ............... Iterate through sub buys meta data for buy "+buyVO.getBuyNumber());
    		while(iterator.hasNext()){
    			SubBuyVO subBuyVO = iterator.next();
    			List<TemplateMetaDataVO> metaDataSubSet = getMetaData(metaDataList, buyVO.getBuyId(), subBuyVO.getSubBuyPK().getSubBuyId());
    			log.info(" ............... Validate buy number for buy "+buyVO.getBuyNumber());
    			if(CoreModelConstants.BASE_SUB_BUY_ID == subBuyVO.getSubBuyPK().getSubBuyId()){
    				boolean isValidaBuy = validateBuyNumber(buyVO, sheet, metaDataSubSet);
    				if(!isValidaBuy){
    					log.error("Invalid Buy Number in the Bid Template Sheet");
    					throw new BidTemplateException("The incorrect Buy Template was selected");    	        	    	        		    	    			
    				}
    			}    
    			
    			List<BidTemplateTO> list = new ArrayList();
    			log.info(" ............... Set imported Bid Lid Data for buy "+buyVO.getBuyNumber());
    			Map lidErrorMap = setBidLidData(sheet,subBuyVO, metaDataSubSet, list);
    			if(!lidErrorMap.isEmpty()){
    				errors.putAll(lidErrorMap);
    				response.setStatus(FBConstants.INPUT_ERROR);
	    			return response;
    			}
    			
    			bidLidDataMap.put(subBuyVO.getSubBuyPK().getSubBuyId(), list);
    		}
    		  
    		response.setBidTemplateMap(bidLidDataMap);
    		response.setStatus(FBConstants.SUCCESS);    		    		
    	}catch(IOException exp){
    		log.error("Error while reading the contents of Bid Template", exp);
    		throw new BidTemplateException("The template was not uploaded because the format was incorrect");    	    		
    	}catch(BidTemplateException exp){
    		log.error(exp);
    		throw exp;
    	}catch(Exception exp){    		
    		log.error("Error while reading the contents of Bid Template", exp);
    		exp.printStackTrace();
    		throw new BidTemplateException(exp);
    	}
						
    	log.info("................. End in BidTemplateHelper.validateAndSetBidLidData ............");
		return response;
	}
    
    private Map<String, String> setBidLidData(Sheet sheet, SubBuyVO subBuyVO, List<TemplateMetaDataVO> metaDataSubSet, List<BidTemplateTO> bidTemplateList) throws BidTemplateException{
    	log.info("................. Begin in BidTemplateHelper.setBidLidData ............for sub buy "+subBuyVO.getSubBuyNumber());
    	Map<String, String> errorMap = new HashMap();
    	Set<BuyLidVO> buyLidSet = subBuyVO.getAuctionLidSet();
    	
    	log.info(" ............... get sub buy line item position for subbuy "+subBuyVO.getSubBuyNumber());
    	int lineItemStartRow = getLineItemStartRow(metaDataSubSet, subBuyVO);
    	
    	int buyItemNoColPosition = -1;
    	int unitPriceColPosition = -1;
    	int inclLineItemColPosition = -1;
    	int manufacturerColPosition = -1;
    	int partNumberColPosition = -1;
    	int sellerDescColPosition = -1;
    	int stopAutoBebitAtColPosition = -1;
    	
    	for(TemplateMetaDataVO metaDataVO : metaDataSubSet){    		   		
    		if(BidTemplateModelConstants.COLUMNS_ENUM.BUY_ITEM_NO.getEntityName().equalsIgnoreCase(metaDataVO.getEntityName())){	    			
    			buyItemNoColPosition = metaDataVO.getColumnPosition();
    		}else if(BidTemplateModelConstants.COLUMNS_ENUM.CONTRACT_PRICE.getEntityName().equalsIgnoreCase(metaDataVO.getEntityName()) ||
    				BidTemplateModelConstants.COLUMNS_ENUM.LINE_ITEM_PRICE.getEntityName().equalsIgnoreCase(metaDataVO.getEntityName()) ||
    				BidTemplateModelConstants.COLUMNS_ENUM.UNIT_PRICE.getEntityName().equalsIgnoreCase(metaDataVO.getEntityName())){    			
    			unitPriceColPosition = metaDataVO.getColumnPosition();
    		}else if(BidTemplateModelConstants.COLUMNS_ENUM.INCLUDED_IN_LINE_ITEM.getEntityName().equalsIgnoreCase(metaDataVO.getEntityName())){
    			inclLineItemColPosition = metaDataVO.getColumnPosition();
    		}else if(BidTemplateModelConstants.COLUMNS_ENUM.MANUFACTURER.getEntityName().equalsIgnoreCase(metaDataVO.getEntityName())){
    			manufacturerColPosition = metaDataVO.getColumnPosition();
    		}else if(BidTemplateModelConstants.COLUMNS_ENUM.PART_NUMBER.getEntityName().equalsIgnoreCase(metaDataVO.getEntityName())){
    			partNumberColPosition = metaDataVO.getColumnPosition();
    		}else if(BidTemplateModelConstants.COLUMNS_ENUM.SELLER_DESCRIPTION.getEntityName().equalsIgnoreCase(metaDataVO.getEntityName())){
    			sellerDescColPosition = metaDataVO.getColumnPosition();
    		}else if(BidTemplateModelConstants.COLUMNS_ENUM.STOP_AUTO_REBID_AT.getEntityName().equalsIgnoreCase(metaDataVO.getEntityName())){
    			stopAutoBebitAtColPosition = metaDataVO.getColumnPosition();
    		}
    	}
    	
    	List<BidTemplateTO> tempBidTeplateList = new ArrayList();
    	StringBuilder bidItemError = new StringBuilder("");
    	log.info(" ............... get lid data for subbuy "+subBuyVO.getSubBuyNumber());
    	for(BuyLidVO buyLidVO : buyLidSet){    
    		BidTemplateTO bidTemplateTO = new BidTemplateTO();
    		bidTemplateTO.setSubBuyId(subBuyVO.getSubBuyPK().getSubBuyId());
    		
    		Row row = sheet.getRow(lineItemStartRow);
    		Cell cell = row.getCell(buyItemNoColPosition);
    		try{
    			Integer bidItemId = new Integer((int)cell.getNumericCellValue());  
    			if(bidItemId.intValue() > 0){
    				bidTemplateTO.setBuyItemId(bidItemId);
    			}else{
    				if(bidItemError.length() == 0){
    					bidItemError.append("Invalid Buy Item No. on row "+(lineItemStartRow+1));
    				}else{
    					bidItemError.append(", row "+(lineItemStartRow+1));
    				}
    			}
    		}catch(Exception exp){
    			log.error("Error while getting bid item id  at row "+lineItemStartRow, exp);
    			if(bidItemError.length() == 0){
					bidItemError.append("Invalid Buy Item No. on row "+(lineItemStartRow+1));
				}else{
					bidItemError.append(", row "+(lineItemStartRow+1));
				}    			
    		}
    		
    		if(unitPriceColPosition != -1){
    			cell = row.getCell(unitPriceColPosition);
	    		try{
	    			BigDecimal unitPrice = BigDecimal.valueOf(cell.getNumericCellValue());    	
	    			bidTemplateTO.setUnitPrice(unitPrice);
	    		}catch(Exception exp){
	    			log.error("Error while getting unit price  at row "+lineItemStartRow+" : "+exp.getMessage());	    			
	    		}
    		}
    		
    		if(inclLineItemColPosition != -1){
    			cell = row.getCell(inclLineItemColPosition);
	    		try{
	    			Integer  inclInLineItem = new Integer((int)cell.getNumericCellValue());    	
	    			bidTemplateTO.setIncludedInLineItem(inclInLineItem);
	    		}catch(Exception exp){
	    			log.error("Error while getting included in line item  at row "+lineItemStartRow+" : "+exp.getMessage());	    
	    		}
    		}
    		
    		if(manufacturerColPosition != -1){
    			cell = row.getCell(manufacturerColPosition);
    			cell.setCellType(Cell.CELL_TYPE_STRING);
	    		try{
	    			String  manufacturer = StringUtil.unNuller(cell.getStringCellValue());  
	    			
	    			String strManufacturerLength = DynamicFilePropertiesManager.
	    					getPropertyConfig(PropertyFileKeyConstants.COMMON_SYSTEM_PROPERTIES).
	    						getString(CommonSystemPropertiesConstants.MANUFACTURER_LENGTH, "");
	    			int manufacturerLength = Integer.parseInt(strManufacturerLength);
	    			
	    			if(manufacturer.length() > manufacturerLength){
	    				manufacturer = manufacturer.substring(0, manufacturerLength);
	    			}
	    			bidTemplateTO.setManufacturer(manufacturer);
	    		}catch(Exception exp){
	    			log.error("Error while getting manufacturer  at row "+lineItemStartRow+" : "+exp.getMessage());	    			
	    		}
    		}
    		
    		if(partNumberColPosition != -1){
    			cell = row.getCell(partNumberColPosition);
    			cell.setCellType(Cell.CELL_TYPE_STRING);
	    		try{	    				    			   		
	    			String partNumber = StringUtil.unNuller(cell.getStringCellValue());    	
	    			String strPartNumberLength = DynamicFilePropertiesManager.
	    					getPropertyConfig(PropertyFileKeyConstants.COMMON_SYSTEM_PROPERTIES).
	    						getString(CommonSystemPropertiesConstants.PART_NUMBER_LENGTH, "");
	    			int partNumberLength = Integer.parseInt(strPartNumberLength);
	    			if(partNumber.length() > partNumberLength){
	    				partNumber = partNumber.substring(0, partNumberLength);
	    			}
	    			
	    			bidTemplateTO.setPartNumber(partNumber);
	    		}catch(Exception exp){
	    			log.error("Error while getting part number  at row "+lineItemStartRow+" : "+exp.getMessage());	    			
	    		}
    		}
    		
    		if(sellerDescColPosition != -1){
    			cell = row.getCell(sellerDescColPosition);
    			cell.setCellType(Cell.CELL_TYPE_STRING);
	    		try{
	    			String  sellerDesc = StringUtil.unNuller(cell.getStringCellValue());    
	    			String strSellerDescLength = DynamicFilePropertiesManager.
	    					getPropertyConfig(PropertyFileKeyConstants.COMMON_SYSTEM_PROPERTIES).
	    						getString(CommonSystemPropertiesConstants.SELLER_DESC_LENGTH, "");
	    			int sellerDescLength = Integer.parseInt(strSellerDescLength);
	    			if(sellerDesc.length() > sellerDescLength){
	    				sellerDesc = sellerDesc.substring(0, sellerDescLength);
	    			}
	    			bidTemplateTO.setSellerDesc(sellerDesc);
	    		}catch(Exception exp){
	    			log.error("Error while getting seller description  at row "+lineItemStartRow+" : "+exp.getMessage());
	    		}
    		}
    		
    		if(stopAutoBebitAtColPosition != -1){
    			cell = row.getCell(stopAutoBebitAtColPosition);
	    		try{
	    			BigDecimal stopAutoRebidAt = BigDecimal.valueOf(cell.getNumericCellValue());    	
	    			bidTemplateTO.setRebidMinimum(stopAutoRebidAt);
	    		}catch(Exception exp){
	    			log.error("Error while getting stop auto rebid at row "+lineItemStartRow+" : "+exp.getMessage());	    			
	    		}
    		}
    		    		
    		tempBidTeplateList.add(bidTemplateTO);
    		lineItemStartRow ++;
    	}
    	
    	if(bidItemError.length() > 0){
    		errorMap.put("InvalidLid", bidItemError.toString());
		}
    	
    	if(!errorMap.isEmpty()){
    		return errorMap;
    	}
    	
    	for(BuyLidVO buyLidVO : buyLidSet){        		
    		Integer itemId = buyLidVO.getItemId();
    		
    		for(BidTemplateTO bidTemplateTO : tempBidTeplateList){
    			if(itemId.equals(bidTemplateTO.getBuyItemId())){
    				bidTemplateList.add(bidTemplateTO);
    				break;
    			}
    		}
    	}
    	    
    	log.info("................. End in BidTemplateHelper.setBidLidData ............");
    	return errorMap;
    }
    
    private int getLineItemStartRow(List<TemplateMetaDataVO> metaDataSubSet, SubBuyVO subBuyVO) throws BidTemplateException{    	
    	for(TemplateMetaDataVO metaDataVO : metaDataSubSet){
    		if((BidTemplateModelConstants.COLUMNS_ENUM.LINE_ITEMS.getEntityName()+"_"+subBuyVO.getSubBuyNumber()).equalsIgnoreCase(metaDataVO.getEntityName())){
    			return metaDataVO.getRowStartPosition();
    		}
    	}
    	
    	throw new BidTemplateException("Row position for "+BidTemplateModelConstants.COLUMNS_ENUM.LINE_ITEMS.getEntityName()+"_"+subBuyVO.getSubBuyNumber()+" does not exist for subBuy "+subBuyVO.getSubBuyPK().getSubBuyId() + " for Buy "+subBuyVO.getSubBuyPK().getBuyVO().getBuyNumber());
    }
    
    private boolean validateBuyNumber(BuyVO buyVO, Sheet sheet, List<TemplateMetaDataVO> metaDataSubSet){
    	for(TemplateMetaDataVO metaDataVO : metaDataSubSet){
			if(BidTemplateModelConstants.COLUMNS_ENUM.BUY_NUMBER.getEntityName().equalsIgnoreCase(metaDataVO.getEntityName())){
				int rowPosition = metaDataVO.getRowStartPosition();
				int columnPosition = metaDataVO.getColumnPosition();
				Row row = sheet.getRow(rowPosition);    						
				Cell cell = row.getCell(columnPosition);
				int bnIndex = cell.getStringCellValue().indexOf(":");
				String buyNumber = StringUtil.unNuller(cell.getStringCellValue().substring(bnIndex+1, cell.getStringCellValue().length()));
				if(buyNumber.equals(buyVO.getBuyNumber())){
					return true;
				}else{
					return false;
				}
			}
		} 
    	
    	return false;
    }
    
    private List<TemplateMetaDataVO> getMetaData(List<TemplateMetaDataVO> metaDataList, Integer buyId, Integer subBuyId){
    	List<TemplateMetaDataVO> metaDataSubList = new ArrayList();
    	for(TemplateMetaDataVO metaDataVO : metaDataList){
    		if(metaDataVO.getBuyId().equals(buyId) && metaDataVO.getSubBuyId().equals(subBuyId)){
    			metaDataSubList.add(metaDataVO);
    		}
    	}
    	
    	return metaDataSubList;
    }
    
    public byte[] getBidDownloadDetails(byte[] bytes, List<TemplateMetaDataVO> metadataList, DraftBidVO bidVO) {
    	
    	try {
    		
    		XSSFWorkbook workBook = new XSSFWorkbook(new ByteArrayInputStream(bytes));
    		
    		Set<DraftSubBidVO> subBidList =  bidVO.getDsubBidSet();
    		if (subBidList != null
    				&& !subBidList.isEmpty()) {
    			
    			for (DraftSubBidVO subBidVO : subBidList) {
    				writeWorkBookBySubBuy(subBidVO, getTemplateMetaDataBySubBuy(metadataList, 
    						subBidVO.getDraftSubBidPK().getSubBidId()), workBook);
    			}
    		}
    		
    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		try {
    			workBook.write(bos);
    		} finally {
    		    bos.close();
    		}
    		return bos.toByteArray();
    		
    		
    	} catch (Exception ex) {
    		log.error(ex.getMessage(), ex);
    	}
    	
    	return null;
    }
    
    
    public byte[] getBidDownloadDetails(byte[] bytes, List<TemplateMetaDataVO> metadataList, BidVO bidVO) {
    	
    	try {
    		
    		XSSFWorkbook workBook = new XSSFWorkbook(new ByteArrayInputStream(bytes));
    		
    		Set<SubBidVO> subBidSet =  bidVO.getSubBidSet();
    		if (subBidSet != null
    				&& !subBidSet.isEmpty()) {
    			
    			for (SubBidVO subBidVO : subBidSet) {
    				writeWorkBookBySubBuy(subBidVO, getTemplateMetaDataBySubBuy(metadataList, 
    						subBidVO.getSubBidPK().getSubBidId()), workBook);
    			}
    		}
    		
    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		try {
    			workBook.write(bos);
    		} finally {
    		    bos.close();
    		}
    		return bos.toByteArray();
    		
    		
    	} catch (Exception ex) {
    		log.error(ex.getMessage(), ex);
    	}
    	
    	return null;
    }
    
    private void writeWorkBookBySubBuy(DraftSubBidVO subBidVO, List<TemplateMetaDataVO> templateList, XSSFWorkbook workBook) {
    	
    	for (TemplateMetaDataVO metaDataVO: templateList) {
    		if (BidTemplateHelper.PART_NUMBER.equalsIgnoreCase(metaDataVO.getEntityName())) {
    			Set<DraftBidLidVO> bidLidSet = subBidVO.getDraftBidLidVO();
    			List<DraftBidLidVO> bidLidList = new ArrayList<DraftBidLidVO>();
    			bidLidList.addAll(bidLidSet);
    			int columnNum = metaDataVO.getColumnPosition();
    			int rowNum = metaDataVO.getRowStartPosition();
    			Sheet  sheet = workBook.getSheetAt(0);
    			for (int i = 0; i < bidLidList.size(); i++) {
    				Row row = sheet.getRow(rowNum);
    				Cell cell = row.getCell(columnNum);
    				if (bidLidList.get(i).getDraftBidLidDetailVOList()  != null
    						&& !bidLidList.get(i).getDraftBidLidDetailVOList().isEmpty()) {
    					
    					cell.setCellValue(bidLidList.get(i).getDraftBidLidDetailVOList().get(0).getPartNumber());
    				}
    				
    				rowNum = rowNum + 1;
    			}
    		}
    		if (BidTemplateHelper.MANUFACTURER.equalsIgnoreCase(metaDataVO.getEntityName())) {
    			Set<DraftBidLidVO> bidLidSet = subBidVO.getDraftBidLidVO();
    			List<DraftBidLidVO> bidLidList = new ArrayList<DraftBidLidVO>();
    			bidLidList.addAll(bidLidSet);
    			int columnNum = metaDataVO.getColumnPosition();
    			int rowNum = metaDataVO.getRowStartPosition();
    			Sheet  sheet = workBook.getSheetAt(0);
    			for (int i = 0; i < bidLidList.size(); i++) {
    				Row row = sheet.getRow(rowNum);
    				Cell cell = row.getCell(columnNum);
    				if (bidLidList.get(i).getDraftBidLidDetailVOList()  != null
    						&& !bidLidList.get(i).getDraftBidLidDetailVOList().isEmpty()) {
    					cell.setCellValue(bidLidList.get(i).getDraftBidLidDetailVOList().get(0).getManufacturer());
    				}
    				
    				rowNum = rowNum + 1;
    			}
    		}
    		if (BidTemplateHelper.SELLER_DETAILS.equalsIgnoreCase(metaDataVO.getEntityName())) {
    			Set<DraftBidLidVO> bidLidSet = subBidVO.getDraftBidLidVO();
    			List<DraftBidLidVO> bidLidList = new ArrayList<DraftBidLidVO>();
    			bidLidList.addAll(bidLidSet);
    			int columnNum = metaDataVO.getColumnPosition();
    			int rowNum = metaDataVO.getRowStartPosition();
    			Sheet  sheet = workBook.getSheetAt(0);
    			for (int i = 0; i < bidLidList.size(); i++) {
    				Row row = sheet.getRow(rowNum);
    				Cell cell = row.getCell(columnNum);
    				cell.setCellValue(bidLidList.get(i).getCriteria());
    				rowNum = rowNum + 1;
    			}
    		}
    		if (BidTemplateHelper.UNIT_PRICE.equalsIgnoreCase(metaDataVO.getEntityName())) {
    			Set<DraftBidLidVO> bidLidSet = subBidVO.getDraftBidLidVO();
    			List<DraftBidLidVO> bidLidList = new ArrayList<DraftBidLidVO>();
    			bidLidList.addAll(bidLidSet);
    			int columnNum = metaDataVO.getColumnPosition();
    			int rowNum = metaDataVO.getRowStartPosition();
    			Sheet  sheet = workBook.getSheetAt(0);
    			for (int i = 0; i < bidLidList.size(); i++) {
    				Row row = sheet.getRow(rowNum);
    				Cell cell = row.getCell(columnNum);
    				if (bidLidList != null && bidLidList.get(i) != null && bidLidList.get(i).getUnitPrice() != null)
    					cell.setCellValue(bidLidList.get(i).getUnitPrice().doubleValue());
    				rowNum = rowNum + 1;
    			}
    		}
    		if (BidTemplateHelper.LINE_ITEM_PRICE.equalsIgnoreCase(metaDataVO.getEntityName())) {
    			Set<DraftBidLidVO> bidLidSet = subBidVO.getDraftBidLidVO();
    			List<DraftBidLidVO> bidLidList = new ArrayList<DraftBidLidVO>();
    			bidLidList.addAll(bidLidSet);
    			int columnNum = metaDataVO.getColumnPosition();
    			int rowNum = metaDataVO.getRowStartPosition();
    			Sheet  sheet = workBook.getSheetAt(0);
    			for (int i = 0; i < bidLidList.size(); i++) {
    				Row row = sheet.getRow(rowNum);
    				Cell cell = row.getCell(columnNum);
    				if (bidLidList != null && bidLidList.get(i) != null && bidLidList.get(i).getUnitPrice() != null)
    					cell.setCellValue(bidLidList.get(i).getUnitPrice().doubleValue());
    				rowNum = rowNum + 1;
    			}
    		}
    		if (BidTemplateHelper.CONTRACT_CEILING_PRICE_INITIAL_UNIT_PRICE.equalsIgnoreCase(metaDataVO.getEntityName())) {
    			Set<DraftBidLidVO> bidLidSet = subBidVO.getDraftBidLidVO();
    			List<DraftBidLidVO> bidLidList = new ArrayList<DraftBidLidVO>();
    			bidLidList.addAll(bidLidSet);
    			int columnNum = metaDataVO.getColumnPosition();
    			int rowNum = metaDataVO.getRowStartPosition();
    			Sheet  sheet = workBook.getSheetAt(0);
    			for (int i = 0; i < bidLidList.size(); i++) {
    				Row row = sheet.getRow(rowNum);
    				Cell cell = row.getCell(columnNum);
    				if (bidLidList != null && bidLidList.get(i) != null && bidLidList.get(i).getUnitPrice() != null)
    					cell.setCellValue(bidLidList.get(i).getUnitPrice().doubleValue());
    				rowNum = rowNum + 1;
    			}
    		}
    		if (BidTemplateHelper.STOP_AUTO_REBID_AT.equalsIgnoreCase(metaDataVO.getEntityName())) {
    			Set<DraftBidLidVO> bidLidSet = subBidVO.getDraftBidLidVO();
    			List<DraftBidLidVO> bidLidList = new ArrayList<DraftBidLidVO>();
    			bidLidList.addAll(bidLidSet);
    			int columnNum = metaDataVO.getColumnPosition();
    			int rowNum = metaDataVO.getRowStartPosition();
    			Sheet  sheet = workBook.getSheetAt(0);
    			for (int i = 0; i < bidLidList.size(); i++) {
    				Row row = sheet.getRow(rowNum);
    				Cell cell = row.getCell(columnNum);
    				if (bidLidList != null && bidLidList.get(i) != null && bidLidList.get(i).getRebidMinimum() != null)
    					cell.setCellValue(bidLidList.get(i).getRebidMinimum().doubleValue());
    				rowNum = rowNum + 1;
    			}
    		}
    		if (BidTemplateHelper.INCLUDED_IN_LINE_ITEM.equalsIgnoreCase(metaDataVO.getEntityName())) {
    			Set<DraftBidLidVO> bidLidSet = subBidVO.getDraftBidLidVO();
    			List<DraftBidLidVO> bidLidList = new ArrayList<DraftBidLidVO>();
    			bidLidList.addAll(bidLidSet);
    			int columnNum = metaDataVO.getColumnPosition();
    			int rowNum = metaDataVO.getRowStartPosition();
    			Sheet  sheet = workBook.getSheetAt(0);
    			for (int i = 0; i < bidLidList.size(); i++) {
    				Row row = sheet.getRow(rowNum);
    				Cell cell = row.getCell(columnNum);
    				if (bidLidList != null && bidLidList.get(i) != null && bidLidList.get(i).getInclItemId() != null)
    					cell.setCellValue(bidLidList.get(i).getInclItemId());
    				rowNum = rowNum + 1;
    			}
    		}
    	}
    }
    
    private void writeWorkBookBySubBuy(SubBidVO subBidVO, List<TemplateMetaDataVO> templateList, XSSFWorkbook workBook) {
    	
    	CellStyle columnStyle = workBook.createCellStyle();
        columnStyle.setBorderLeft(CellStyle.BORDER_THIN);
        columnStyle.setBorderRight(CellStyle.BORDER_THIN);
        columnStyle.setBorderBottom(CellStyle.BORDER_THIN);
        columnStyle.setBorderTop(CellStyle.BORDER_THIN);
        columnStyle.setAlignment(CellStyle.ALIGN_LEFT);
        columnStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        columnStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        columnStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        columnStyle.setWrapText(true);
        columnStyle.setShrinkToFit(true);
        columnStyle.setLocked(true);
    	
    	for (TemplateMetaDataVO metaDataVO: templateList) {
    		if (BidTemplateHelper.PART_NUMBER.equalsIgnoreCase(metaDataVO.getEntityName())) {
    			List<BidLidVO> bidLidList = subBidVO.getBidLidSet();    			
    			int columnNum = metaDataVO.getColumnPosition();
    			int rowNum = metaDataVO.getRowStartPosition();
    			Sheet  sheet = workBook.getSheetAt(0);
    			for (int i = 0; i < bidLidList.size(); i++) {
    				Row row = sheet.getRow(rowNum);
    				Cell cell = row.getCell(columnNum);
    				if (bidLidList.get(i).getBidLidDetailSet()  != null
    						&& !bidLidList.get(i).getBidLidDetailSet().isEmpty()) {
    					
    					cell.setCellValue(bidLidList.get(i).getBidLidDetailSet().iterator().next().getPartNumber());
    				}
    				
    				rowNum = rowNum + 1;
    			}
    		}
    		if (BidTemplateHelper.MANUFACTURER.equalsIgnoreCase(metaDataVO.getEntityName())) {
    			List<BidLidVO> bidLidList = subBidVO.getBidLidSet();
    			int columnNum = metaDataVO.getColumnPosition();
    			int rowNum = metaDataVO.getRowStartPosition();
    			Sheet  sheet = workBook.getSheetAt(0);
    			for (int i = 0; i < bidLidList.size(); i++) {
    				Row row = sheet.getRow(rowNum);
    				Cell cell = row.getCell(columnNum);
    				if (bidLidList.get(i).getBidLidDetailSet()  != null
    						&& !bidLidList.get(i).getBidLidDetailSet().isEmpty()) {
    					cell.setCellValue(bidLidList.get(i).getBidLidDetailSet().iterator().next().getManufacturer());
    				}
    				
    				rowNum = rowNum + 1;
    			}
    		}
    		if (BidTemplateHelper.SELLER_DETAILS.equalsIgnoreCase(metaDataVO.getEntityName())) {
    			List<BidLidVO> bidLidList = subBidVO.getBidLidSet();
    			int columnNum = metaDataVO.getColumnPosition();
    			int rowNum = metaDataVO.getRowStartPosition();
    			Sheet  sheet = workBook.getSheetAt(0);
    			for (int i = 0; i < bidLidList.size(); i++) {
    				Row row = sheet.getRow(rowNum);
    				Cell cell = row.getCell(columnNum);
    				cell.setCellValue(bidLidList.get(i).getCriteria());
    				rowNum = rowNum + 1;
    			}
    		}
    		if (BidTemplateHelper.UNIT_PRICE.equalsIgnoreCase(metaDataVO.getEntityName())) {
    			List<BidLidVO> bidLidList = subBidVO.getBidLidSet();
    			int columnNum = metaDataVO.getColumnPosition();
    			int rowNum = metaDataVO.getRowStartPosition();
    			Sheet  sheet = workBook.getSheetAt(0);
    			for (int i = 0; i < bidLidList.size(); i++) {
    				Row row = sheet.getRow(rowNum);
    				Cell cell = row.getCell(columnNum);
    				cell.setCellStyle(columnStyle);
    				if (bidLidList != null && bidLidList.get(i) != null && bidLidList.get(i).getUnitPrice() != null)
    					cell.setCellValue(bidLidList.get(i).getUnitPrice().doubleValue());
    				rowNum = rowNum + 1;
    			}
    		}
    		if (BidTemplateHelper.LINE_ITEM_PRICE.equalsIgnoreCase(metaDataVO.getEntityName())) {
    			List<BidLidVO> bidLidList = subBidVO.getBidLidSet();
    			int columnNum = metaDataVO.getColumnPosition();
    			int rowNum = metaDataVO.getRowStartPosition();
    			Sheet  sheet = workBook.getSheetAt(0);
    			for (int i = 0; i < bidLidList.size(); i++) {
    				Row row = sheet.getRow(rowNum);
    				Cell cell = row.getCell(columnNum);
    				cell.setCellStyle(columnStyle);
    				if (bidLidList != null && bidLidList.get(i) != null && bidLidList.get(i).getUnitPrice() != null)
    					cell.setCellValue(bidLidList.get(i).getUnitPrice().doubleValue());
    				rowNum = rowNum + 1;
    			}
    		}
    		if (BidTemplateHelper.CONTRACT_CEILING_PRICE_INITIAL_UNIT_PRICE.equalsIgnoreCase(metaDataVO.getEntityName())) {
    			List<BidLidVO> bidLidList = subBidVO.getBidLidSet();
    			int columnNum = metaDataVO.getColumnPosition();
    			int rowNum = metaDataVO.getRowStartPosition();
    			Sheet  sheet = workBook.getSheetAt(0);
    			for (int i = 0; i < bidLidList.size(); i++) {
    				Row row = sheet.getRow(rowNum);
    				Cell cell = row.getCell(columnNum);
    				cell.setCellStyle(columnStyle);    				
    				if (bidLidList != null && bidLidList.get(i) != null && bidLidList.get(i).getUnitPrice() != null)
    					cell.setCellValue(bidLidList.get(i).getUnitPrice().doubleValue());
    				rowNum = rowNum + 1;
    			}
    		}
    		if (BidTemplateHelper.STOP_AUTO_REBID_AT.equalsIgnoreCase(metaDataVO.getEntityName())) {
    			List<BidLidVO> bidLidList = subBidVO.getBidLidSet();
    			int columnNum = metaDataVO.getColumnPosition();
    			int rowNum = metaDataVO.getRowStartPosition();
    			Sheet  sheet = workBook.getSheetAt(0);
    			for (int i = 0; i < bidLidList.size(); i++) {
    				Row row = sheet.getRow(rowNum);
    				Cell cell = row.getCell(columnNum);
    				if (bidLidList != null && bidLidList.get(i) != null && bidLidList.get(i).getRebidMinimum() != null)
    					cell.setCellValue(bidLidList.get(i).getRebidMinimum().doubleValue());
    				rowNum = rowNum + 1;
    			}
    		}
    		if (BidTemplateHelper.INCLUDED_IN_LINE_ITEM.equalsIgnoreCase(metaDataVO.getEntityName())) {
    			List<BidLidVO> bidLidList = subBidVO.getBidLidSet();
    			int columnNum = metaDataVO.getColumnPosition();
    			int rowNum = metaDataVO.getRowStartPosition();
    			Sheet  sheet = workBook.getSheetAt(0);
    			for (int i = 0; i < bidLidList.size(); i++) {
    				Row row = sheet.getRow(rowNum);
    				Cell cell = row.getCell(columnNum);
    				if (bidLidList != null && bidLidList.get(i) != null && bidLidList.get(i).getInclItemId() != null)
    					cell.setCellValue(bidLidList.get(i).getInclItemId());
    				rowNum = rowNum + 1;
    			}
    		}
    	}
    }
    
    private List<TemplateMetaDataVO> getTemplateMetaDataBySubBuy(List<TemplateMetaDataVO> metadataList, Integer subBuyId) {
    	List<TemplateMetaDataVO> tempList = new ArrayList<TemplateMetaDataVO>();
    	for (TemplateMetaDataVO metaDataVO : metadataList) {
    		if (metaDataVO.getSubBuyId().equals(subBuyId)) {
    			tempList.add(metaDataVO);
    		}
    	}
    	return tempList;
    }
}
