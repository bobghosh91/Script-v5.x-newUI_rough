package appSpecificFunctions

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.util.List

import javax.json.Json;
import javax.json.JsonArray
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder
import javax.json.JsonReader;

import org.openqa.selenium.JavascriptExecutor

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

import org.json.JSONObject;
import org.json.JSONArray;
import org.apache.commons.io.IOUtils;
//import org.eclipse.persistence.internal.oxm.record.json.JSONParser.object_return



public class RpdProcessing {


	AEAPIConsumption appEngineApi = new AEAPIConsumption()


	/**
	 * Retrieve Catalogs Json content as String.
	 */
	@Keyword
	def String getCatalogsJsonAsString(){

		def rpdJsonBuilderObj = new groovy.json.JsonBuilder(appEngineApi.getRpdContent())

		def slurper = new groovy.json.JsonSlurper()
		def rpdJsonSlurpedObject = slurper.parseText(rpdJsonBuilderObj.toString())

		def catalogsRpdJsonBuilderObj = new groovy.json.JsonBuilder(rpdJsonSlurpedObject.Catalogs)

		return (catalogsRpdJsonBuilderObj.toString())
	}

	/**
	 * Retrieve Catalog code and version as String. This method is currently not used.
	 */
	@Keyword
	def String setCatalogCodeAndVersionInGlobalVariables(){

		def rpdJsonBuilderObj = new groovy.json.JsonBuilder(appEngineApi.getRpdContent())
		def slurper = new groovy.json.JsonSlurper()
		def rpdJsonSlurpedObject = slurper.parseText(rpdJsonBuilderObj.toString())

		def catalogsRpdJsonBuilderObj = new groovy.json.JsonBuilder(rpdJsonSlurpedObject.Catalogs)
		def catalogsRpdJsonSlurpedObj = slurper.parseText(catalogsRpdJsonBuilderObj.toString())

		println (catalogsRpdJsonSlurpedObj.toString())
		println (catalogsRpdJsonSlurpedObj.DefaultFOState)

		String catalogCode = ""
		String catalogVersion = ""

		for (Object o : catalogsRpdJsonSlurpedObj) {

			//println (o.toString())
			//println (o.getClass())

			if (o.getValue().toString().contains("DefaultFOState")) {

				//println o.getValue()
				//println o.getValue().getClass()
				//println o.getValue().get("DefaultFOState")

				catalogCode = (new groovy.json.JsonBuilder(o.getValue().get("Code"))).toString()
				catalogVersion = (new groovy.json.JsonBuilder(o.getValue().get("Version"))).toString()
				break
			}
		}

		if (catalogCode.isEmpty()) {
			GlobalVariable.envInfo.put("catalogCode", "Unavailable - see other tests")
			GlobalVariable.envInfo.put("catalogVersion", "Unavailable - see other tests")
		}
		else {
			GlobalVariable.envInfo.put("catalogCode", catalogCode)
			GlobalVariable.envInfo.put("catalogVersion", catalogVersion)
		}

		println GlobalVariable.envInfo.get("catalogCode")
		println GlobalVariable.envInfo.get("catalogVersion")
	}

	/**
	 * Retrieve Default FOState from Catalogs Json content as String.
	 */
	@Keyword
	def String getDefaultFOStateFromCatalogsJsonAsString(){

		def rpdJsonBuilderObj = new groovy.json.JsonBuilder(appEngineApi.getRpdContent())
		def slurper = new groovy.json.JsonSlurper()
		def rpdJsonSlurpedObject = slurper.parseText(rpdJsonBuilderObj.toString())

		def catalogsRpdJsonBuilderObj = new groovy.json.JsonBuilder(rpdJsonSlurpedObject.Catalogs)
		def catalogsRpdJsonSlurpedObj = slurper.parseText(catalogsRpdJsonBuilderObj.toString())

		println (catalogsRpdJsonSlurpedObj.toString())
		println (catalogsRpdJsonSlurpedObj.DefaultFOState)

		String defaultFOState = ""

		for (Object o : catalogsRpdJsonSlurpedObj) {

			//println (o.toString())
			//println (o.getClass())

			if (o.getValue().toString().contains("DefaultFOState")) {

				//println o.getValue()
				//println o.getValue().getClass()
				//println o.getValue().get("DefaultFOState")

				defaultFOState = (new groovy.json.JsonBuilder(o.getValue().get("DefaultFOState"))).toString()
				break
			}
		}

		//println defaultFOState
		return defaultFOState
	}

	/**
	 * Retrieve Default FOState from Catalogs Json content as String for the catalog,
	 * whose Catalog-ID is provided and has non-null defaultFOState values.
	 */
	@Keyword
	def String getDefaultFOStateFromCatalogsJsonAsString(String CatCode ){   //String FOCombination

		def rpdJsonBuilderObj = new groovy.json.JsonBuilder(appEngineApi.getRpdContent())
		def slurper = new groovy.json.JsonSlurper()
		def rpdJsonSlurpedObject = slurper.parseText(rpdJsonBuilderObj.toString())

		def catalogsRpdJsonBuilderObj = new groovy.json.JsonBuilder(rpdJsonSlurpedObject.Catalogs)
		def catalogsRpdJsonSlurpedObj = slurper.parseText(catalogsRpdJsonBuilderObj.toString())

		println rpdJsonSlurpedObject.Catalogs.getClass().toString()
		println (catalogsRpdJsonSlurpedObj.toString())
		println (catalogsRpdJsonSlurpedObj.DefaultFOState)

		String defaultFOState = ""

		int count=0

		for (Object o : catalogsRpdJsonSlurpedObj) {

			//			println (o.toString())
			//			println (o.getClass())


			if (o.getValue().get("Code").toString().equalsIgnoreCase(CatCode) && o.getValue().toString().contains("DefaultFOState")) {

				//println o.getValue()
				//println o.getValue().getClass()
				println(o.getValue().get("DefaultFOState"))

				defaultFOState = (new groovy.json.JsonBuilder(o.getValue().get("DefaultFOState"))).toString()

				try{
					if(defaultFOState!=null && defaultFOState!= "{}" ){
						count++
					}
				}
				catch(NullPointerException ne){

				}
			}
		}

		if(count==1)
			return defaultFOState
		else
			KeywordUtil.markErrorAndStop("Expected number of non-null DefaultFOState block in RPD is 1. Here "+ count+" non-null DefaultFOState blocks can be seen.")
	}

	@Keyword
	def HashMap <String, String> getDefaultFOStateModifiedFeatures (String fOStateBeforeMofification, String fOStateAfterMofification){

		fOStateBeforeMofification = fOStateBeforeMofification.replace('{',"")
		fOStateBeforeMofification = fOStateBeforeMofification.replaceAll("}","")

		fOStateAfterMofification = fOStateAfterMofification.replace('{',"")
		fOStateAfterMofification = fOStateAfterMofification.replaceAll("}","")

		List<String> fOStateBeforeMofificationInList = fOStateBeforeMofification.split(",")
		List<String> fOStateAfterMofificationInList = fOStateAfterMofification.split(",")

		int expectedFOCombinationsSize  = fOStateBeforeMofificationInList.size()
		int actualFOCombinationsSize = fOStateAfterMofificationInList.size()

		HashMap <String, String> modifiedFeatures = new HashMap <String, String> ()

		for (String combinationBefore : fOStateBeforeMofificationInList){

			if (fOStateAfterMofification.contains(combinationBefore) == false) {

				for (String combinationAfter : fOStateAfterMofificationInList){

					if (combinationAfter.contains(combinationBefore.split(":")[0])) {

						String key = combinationAfter.split(":")[0]
						String value = combinationAfter.split(":")[1]
						modifiedFeatures.put(key, value)
					}
				}
			}
		}

		return modifiedFeatures
	}

	/**
	 * Retrieve entire RPD content as String.
	 */
	@Keyword
	def String getEntireJsonAsString(){

		def rpdJsonBuilderObj = new groovy.json.JsonBuilder(appEngineApi.getRpdContent())
		return (rpdJsonBuilderObj.toString())
	}

	/**
	 * Retrieve Products content as String.
	 */
	@Keyword
	def String getProductsJsonAsString(){

		def rpdJsonBuilderObj = new groovy.json.JsonBuilder(appEngineApi.getRpdContent())
		def slurper = new groovy.json.JsonSlurper()
		def rpdJsonSlurpedObject = slurper.parseText(rpdJsonBuilderObj.toString())

		def productsRpdJsonBuilderObj = new groovy.json.JsonBuilder(rpdJsonSlurpedObject.Products)

		return (productsRpdJsonBuilderObj.toString())
	}

	/**
	 * Retrieve Items content as String.
	 */
	@Keyword
	def String getItemsJsonAsString(){

		def rpdJsonBuilderObj = new groovy.json.JsonBuilder(appEngineApi.getRpdContent())
		def slurper = new groovy.json.JsonSlurper()
		def rpdJsonSlurpedObject = slurper.parseText(rpdJsonBuilderObj.toString())

		def itemsRpdJsonBuilderObj = new groovy.json.JsonBuilder(rpdJsonSlurpedObject.Items)

		return (itemsRpdJsonBuilderObj.toString())
	}

	/**
	 * Retrieve Item IDs based on items type
	 */
	@Keyword
	def ArrayList <String> getItemIDsBasedOnItemTypes(String itemType){

		RpdProcessing rpdProcessing = new RpdProcessing()

		String allItems = rpdProcessing.getItemsJsonAsString()

		println allItems

		ArrayList <String> itemList = new ArrayList <String> ()

		def slurper = new groovy.json.JsonSlurper()
		def all3dItemsJsonSlurpedObject = slurper.parseText(allItems)

		println all3dItemsJsonSlurpedObject.getClass()

		// ------------ Selection based on item type, 1st encountered item will be selected

		String expectedItemType = itemType
		String itemCode = ''

		for (def object : all3dItemsJsonSlurpedObject) {

			//println object
			//println object.getClass()

			for (entry in object) {

				//println entry.key
				//println entry.value
				//println entry.value.getClass()
				//println entry.value.get('Type')

				if ((entry.value.get('Type').toString()).equals(expectedItemType)) {
					//itemCode = entry.key
					itemList.add(entry.key)
				}
			}

		}

		return itemList
	}
	/**
	 * Retrieve Item IDs based on items Code
	 */
	@Keyword
	def ArrayList <String> getItemIDsBasedOnItemCode(String itemCode){

		RpdProcessing rpdProcessing = new RpdProcessing()

		String allItems = rpdProcessing.getItemsJsonAsString()

		println allItems

		ArrayList <String> itemList = new ArrayList <String> ()

		def slurper = new groovy.json.JsonSlurper()
		def all3dItemsJsonSlurpedObject = slurper.parseText(allItems)
		println all3dItemsJsonSlurpedObject.getClass()
		String expectedItemCode = itemCode


		for (def object : all3dItemsJsonSlurpedObject) {
			for (entry in object) {
				if ((entry.value.get('Code').toString()).equals(expectedItemCode)) {
					//itemCode = entry.key
					itemList.add(entry.key)
				}
			}

		}

		return itemList
	}

	/**
	 * Retrieve Viewing Properties content as String.
	 */
	@Keyword
	def String getViewingPropertiesJsonAsString(){

		def rpdJsonBuilderObj = new groovy.json.JsonBuilder(appEngineApi.getRpdContent())
		def slurper = new groovy.json.JsonSlurper()
		def rpdJsonSlurpedObject = slurper.parseText(rpdJsonBuilderObj.toString())

		def viewingPropertiesRpdJsonBuilderObj = new groovy.json.JsonBuilder(rpdJsonSlurpedObject.ViewingProps)

		return (viewingPropertiesRpdJsonBuilderObj.toString())
	}

	/**
	 * Retrieve Version as String.
	 */
	@Keyword
	def String getVersionAsString(){

		def rpdJsonBuilderObj = new groovy.json.JsonBuilder(appEngineApi.getRpdContent())
		def slurper = new groovy.json.JsonSlurper()
		def rpdJsonSlurpedObject = slurper.parseText(rpdJsonBuilderObj.toString())

		def versionOfRpdJsonBuilderObj = new groovy.json.JsonBuilder(rpdJsonSlurpedObject.Version)

		return (versionOfRpdJsonBuilderObj.toString())
	}

	/**
	 * Retrieve Products Count
	 */
	@Keyword
	def int getProductsCount(){

		def rpdJsonBuilderObj = new groovy.json.JsonBuilder(appEngineApi.getRpdContent())
		def slurper = new groovy.json.JsonSlurper()
		def rpdJsonSlurpedObject = slurper.parseText(rpdJsonBuilderObj.toString())

		int count = 0
		for (def product : rpdJsonSlurpedObject.Products) {

			//println (product)
			count = count + 1
		}

		//println ("Total Products: " + count)
		return count
	}

	/**
	 * Retrieve Items Count
	 */
	@Keyword
	def int getItemsCount(){

		def rpdJsonBuilderObj = new groovy.json.JsonBuilder(appEngineApi.getRpdContent())
		def slurper = new groovy.json.JsonSlurper()
		def rpdJsonSlurpedObject = slurper.parseText(rpdJsonBuilderObj.toString())

		int count = 0
		for (def item : rpdJsonSlurpedObject.Items) {

			//println (item)
			count = count + 1
		}

		println ("Total Items: " + count)
		return count
	}

	/**
	 * Retrieve Individual Product Information Json Count, based on RPD Identifier Key For Product Section
	 */
	@Keyword
	def String getProductInformationAsStringFromRpdProductKey (String rpdIdentifierKeyForProductSection){

		def rpdJsonBuilderObj = new groovy.json.JsonBuilder(appEngineApi.getRpdContent())
		def slurper = new groovy.json.JsonSlurper()
		def rpdJsonSlurpedObject = slurper.parseText(rpdJsonBuilderObj.toString())

		String individualProductInformationAsString = ""

		for (def product : rpdJsonSlurpedObject.Products) {

			if (product.getKey().toString().equals(rpdIdentifierKeyForProductSection)) {

				individualProductInformationAsString = product.getValue().toString()
				//println individualProductInformationAsString
				break
			}
		}

		return individualProductInformationAsString
	}

	/**
	 * Retrieve Specific Room Dimension. Supported inputs are W, H, D.
	 */
	@Keyword
	def String getSpecifiedRoomDimension(String dimensionType){

		def rpdJsonBuilderObj = new groovy.json.JsonBuilder(appEngineApi.getRpdContent())
		def slurper = new groovy.json.JsonSlurper()
		def rpdJsonSlurpedObject = slurper.parseText(rpdJsonBuilderObj.toString())
		//rpdJsonSlurpedObject.Floors.get(0).get('Rooms').get(0).get('Dimension').get("W")

		switch (dimensionType)
		{
			case 'D' :
			case 'd' :
				return rpdJsonSlurpedObject.Floors[0].Rooms[0].Dimension.D
			case 'H' :
			case 'h' :
				return rpdJsonSlurpedObject.Floors[0].Rooms[0].Dimension.H
			case 'W' :
			case 'w' :
				return rpdJsonSlurpedObject.Floors[0].Rooms[0].Dimension.W
			default :
				throw Exception
		}
	}

	/**
	 * Retrieve Item Code Based On ItemType And Property
	 */
	@Keyword
	def String getItemIdBasedOnItemTypeAndPropertyList (String itemType, List<String> expectedKeyPropertyPairList){

		RpdProcessing rpdProcessing = new RpdProcessing()

		String all3dItems = rpdProcessing.getItemsJsonAsString()

		println all3dItems

		def slurper = new groovy.json.JsonSlurper()
		def all3dItemsJsonSlurpedObject = slurper.parseText(all3dItems)

		println all3dItemsJsonSlurpedObject.getClass()

		// ------------ Selection based on item type and property combination

		List <String> actualKeyPropertyPairList = new ArrayList <String> ()

		String expectedItemType = itemType
		String itemId = ''

		for (def object : all3dItemsJsonSlurpedObject) {

			actualKeyPropertyPairList.clear()

			//println object
			//println object.getClass()

			for (entry in object) {

				//println entry.key
				//println entry.value
				//println entry.value.getClass()
				//println entry.value.get('Props')
				//println entry.value.get('Props').getClass()

				if ((entry.value.get('Type').toString()).equals(expectedItemType)) {

					//itemCode = entry.key

					for (propertyEntry in entry.value.get('Props')) {

						String actualKeyPropertyPair = propertyEntry.key + ':' + propertyEntry.value
						actualKeyPropertyPair = actualKeyPropertyPair.trim().replaceAll(" ", "")
						println actualKeyPropertyPair

						actualKeyPropertyPairList.add(actualKeyPropertyPair)
					}

					int propertyMatchCount = 0

					for (String expectedKeyPropertyPair : expectedKeyPropertyPairList) {

						expectedKeyPropertyPair = expectedKeyPropertyPair.trim().replaceAll(" ", "")

						if (actualKeyPropertyPairList.contains(expectedKeyPropertyPair)) {

							propertyMatchCount = propertyMatchCount + 1
						}
					}

					if (propertyMatchCount == expectedKeyPropertyPairList.size()) {

						itemId = entry.key
						break
					}
				}

				if (itemId.length() != 0) {
					break
				}

			}

			if (itemId.length() != 0) {
				break
			}
		}

		if (itemId.length() != 0) {
			return itemId
		}
		else {
			return null
		}
	}

	/**
	 * Retrieve Item Code Based On ItemType And Property
	 */
	@Keyword
	def String getItemIdBasedOnItemTypeAndProperty(String itemType, String propertyCombination){

		RpdProcessing rpdProcessing = new RpdProcessing()

		String all3dItems = rpdProcessing.getItemsJsonAsString()

		println all3dItems

		def slurper = new groovy.json.JsonSlurper()
		def all3dItemsJsonSlurpedObject = slurper.parseText(all3dItems)

		println all3dItemsJsonSlurpedObject.getClass()

		// ------------ Selection based on item type and property combination

		String expectedKeyPropertyPair = propertyCombination
		String expectedItemType = itemType
		String itemId = ''

		for (def object : all3dItemsJsonSlurpedObject) {

			//println object
			//println object.getClass()

			for (entry in object) {

				//println entry.key
				//println entry.value
				//println entry.value.getClass()
				//println entry.value.get('Props')
				//println entry.value.get('Props').getClass()

				if ((entry.value.get('Type').toString()).equals(expectedItemType)) {

					//itemCode = entry.key

					for (propertyEntry in entry.value.get('Props')) {

						String actualKeyPropertyPair = propertyEntry.key + ':' + propertyEntry.value
						actualKeyPropertyPair = actualKeyPropertyPair.trim().replaceAll(" ", "")
						println actualKeyPropertyPair

						expectedKeyPropertyPair = expectedKeyPropertyPair.trim().replaceAll(" ", "")

						if (actualKeyPropertyPair.equals(expectedKeyPropertyPair)) {
							itemId = entry.key
							break
						}
					}

					if (itemId.length() != 0) {
						break
					}
				}

				if (itemId.length() != 0) {
					break
				}

			}

			if (itemId.length() != 0) {
				break
			}
		}

		if (itemId.length() != 0) {
			return itemId
		}
		else {
			return null
		}
	}
	/**
	 * Retrieve Item Property Based On ItemID 
	 */
	@Keyword
	def String getItemPropertyBasedOnItemId(String itemID,String propertyName){
		RpdProcessing rpdProcessing = new RpdProcessing()
		String all3dItems = rpdProcessing.getItemsJsonAsString()
		//println all3dItems
		def slurper = new groovy.json.JsonSlurper()
		def all3dItemsJsonSlurpedObject = slurper.parseText(all3dItems)
		//println all3dItemsJsonSlurpedObject.getClass()
		String expectedPropertyValue=''
		String propertyValue = ''
		for (def object : all3dItemsJsonSlurpedObject) {
			for (entry in object) {
				//println entry.key
				if(entry.key.toString().equalsIgnoreCase(itemID))
				{
					propertyValue = entry.value.get('Props').get(propertyName)
					return propertyValue
				}
			}
		}
		return null
	}

	/**
	 * Retrieve Item Code Based On ItemType
	 */
	@Keyword
	def String getItemIdBasedOnItemType(String itemType){

		RpdProcessing rpdProcessing = new RpdProcessing()

		String all3dItems = rpdProcessing.getItemsJsonAsString()

		println all3dItems

		def slurper = new groovy.json.JsonSlurper()
		def all3dItemsJsonSlurpedObject = slurper.parseText(all3dItems)

		println all3dItemsJsonSlurpedObject.getClass()

		// ------------ Selection based on item type, 1st encountered item will be selected

		String expectedItemType = itemType
		String itemId = ''

		for (def object : all3dItemsJsonSlurpedObject) {

			//println object
			//println object.getClass()

			for (entry in object) {

				//println entry.key
				//println entry.value
				//println entry.value.getClass()
				//println entry.value.get('Type')

				if ((entry.value.get('Type').toString()).equals(expectedItemType)) {
					itemId = entry.key
					break
				}
			}

			if (itemId.length() != 0) {
				break
			}
		}

		if (itemId.length() != 0) {
			return itemId
		}
		else {
			return null
		}
	}

	/**
	 * Retrieve Item Code Based On ProductCode And Property
	 */
	@Keyword
	def String getItemIdBasedOnProductCodeAndProperty(String productCode, String propertyCombination) {

		RpdProcessing rpdProcessing = new RpdProcessing()

		String all3dItems = rpdProcessing.getItemsJsonAsString()

		println all3dItems

		def slurper = new groovy.json.JsonSlurper()
		def all3dItemsJsonSlurpedObject = slurper.parseText(all3dItems)

		println all3dItemsJsonSlurpedObject.getClass()

		// ------------ Selection based on product code and property combination, 1st encountered item will be selected

		String expectedKeyPropertyPair = propertyCombination
		String expectedProductCode = productCode
		String itemId = ''

		for (def object : all3dItemsJsonSlurpedObject) {

			//println object
			//println object.getClass()

			for (entry in object) {

				//println entry.key
				//println entry.value
				//println entry.value.getClass()
				//println entry.value.get('Props')
				//println entry.value.get('Props').getClass()

				if ((entry.value.get('Code').toString()).equals(expectedProductCode)) {

					itemId = entry.key

					for (propertyEntry in entry.value.get('Props')) {

						String actualKeyPropertyPair = propertyEntry.key + ':' + propertyEntry.value
						actualKeyPropertyPair = actualKeyPropertyPair.trim().replaceAll(" ", "")
						println actualKeyPropertyPair

						expectedKeyPropertyPair = expectedKeyPropertyPair.trim().replaceAll(" ", "")

						if (actualKeyPropertyPair.equals(expectedKeyPropertyPair)) {
							itemId = entry.key
							break
						}
					}

					if (itemId.length() != 0) {
						break
					}
				}

				if (itemId.length() != 0) {
					break
				}

			}

			if (itemId.length() != 0) {
				break
			}
		}

		if (itemId.length() != 0) {
			return itemId
		}
		else {
			return null
		}

	}

	@Keyword
	def String getFirstItemIdBasedOnProductCode(String productCode) {

		RpdProcessing rpdProcessing = new RpdProcessing()

		String all3dItems = rpdProcessing.getProductsJsonAsString()
		println all3dItems
		def slurper = new groovy.json.JsonSlurper()
		def all3dItemsJsonSlurpedObject = slurper.parseText(all3dItems)

		println all3dItemsJsonSlurpedObject.getClass()

		// ------------ Selection based on product code and property combination, 1st encountered item will be selected


		String expectedProductCode = productCode
		String itemId = ''

		for (def object : all3dItemsJsonSlurpedObject) {

			//println object
			//println object.getClass()

			for (entry in object) {

				//				println entry.key
				//				println entry.value
				//				println entry.value.getClass()
				//				println entry.value.get('Props')
				//				println entry.value.get('Props').getClass()
				//				println entry.value.get('Code').toString()
				if ((entry.value.get('Code').toString()).equals(expectedProductCode)) {

					itemId = entry.key
					//					println entry.value.get('ItemUIDs').getClass()
					return entry.value.get('ItemUIDs').get(0).toString()
				}
				if (itemId.length() != 0) {
					break
				}
			}
			if (itemId.length() != 0) {
				break
			}
		}
		if (itemId.length() != 0) {
			return itemId
		}
		else {
			return null
		}
	}




	// ----------------------------------------------------------------------
	// R&D RPD validation

	/**
	 * Load RPD design from file
	 * @param fileToLoad from hard drive location
	 */
	@Keyword
	def loadRPDfromFile(fileToLoad) {

		JavascriptExecutor js = ((DriverFactory.getWebDriver()) as JavascriptExecutor)

		JsonReader reader = Json.createReader(new FileReader(fileToLoad))

		JsonObject RPDfromFile = reader.readObject()

		String jsRPDtoLoad = ('var callback = arguments[arguments.length - 1]; AE.provisional.design.load(' + RPDfromFile.toString()) +
				', null, DesignData => callback(DesignData))'

		js.executeAsyncScript(jsRPDtoLoad)
	}

	/**
	 * Get HashMap from JSON (RPD)
	 */
	@Keyword
	def Map getHashMapFromJSON() {

		JavascriptExecutor js = ((DriverFactory.getWebDriver()) as JavascriptExecutor)

		String jsStmt = 'var callback = arguments[arguments.length - 1]; AE.provisional.design.serialize({toJSON: true}, i_oDesignData => callback(i_oDesignData));'

		def rpdJsonObj = new groovy.json.JsonBuilder(js.executeAsyncScript(jsStmt))

		def slurper = new groovy.json.JsonSlurper()

		return slurper.parseText(rpdJsonObj.toString())
	}

	/**
	 * Get catalog DefaultFOState from JSON (RPD)
	 */
	@Keyword
	def Map getCatalogDefaultFOState() {

		def hashMapfromRPD = getHashMapFromJSON()

		Map myCatalog = hashMapfromRPD.get("Catalogs")

		Map catalogID

		for(String key: myCatalog.keySet())

			catalogID = myCatalog.get(key)

		//Map defaultFOState = catalogID.get("DefaultFOState")

		return catalogID.get("DefaultFOState")
	}

	/**
	 * Select an item in RPD / design
	 * @param item to be selected
	 */
	@Keyword
	def selectItemIn3Dviewer(item) {

		JavascriptExecutor js = ((DriverFactory.getWebDriver()) as JavascriptExecutor)

		String scriptToExecute = 'var callback = arguments[arguments.length - 1]; AE.provisional.design.selectItems(["' + item + '"] , DesignData => callback(DesignData))'

		js.executeAsyncScript(scriptToExecute)

		println('Selected item : ' + item)
	}

	/**
	 * Create product list from JSON
	 * @param jsonString JSON file
	 */
	@Keyword
	def String[] createProductListFromJSON() {

		Map rpdJson = getHashMapFromJSON()

		return rpdJson.Floors.Rooms.ProductUIDs.get(0).get(0)
	}

	/**
	 * Convert RPD in JSON format > Second try
	 */
	@Keyword
	def String getLastReplacedProduct() {

		String [] allProduct = createProductListFromJSON()

		println (allProduct)

		//String lastProduct = allProduct[allProduct.size() - 1]

		return allProduct[allProduct.size() - 1]
	}

	/**
	 * Get product code from HashMap/RPD
	 * * @param product to get the code from
	 */
	@Keyword
	def String getProductCodeFromRPD(product) {

		Map hashMapfromRPD = getHashMapFromJSON()

		Map myProducts = hashMapfromRPD.get("Products")

		Map myProduct = myProducts.get(product)

		return myProduct.get("Code")
	}

	/**
	 * Get values from a txt file
	 * @param fileToLoadFrom path to txt file
	 */
	@Keyword
	def String [] loadValuesToCompare(fileToLoadFrom) {

		String values = ''

		File stringFromFile = new File(fileToLoadFrom)

		BufferedReader valueReader = new BufferedReader(new FileReader(stringFromFile));

		String readLine = ""

		while ((readLine = valueReader.readLine()) != null) {
			values = values + readLine
		}

		// TODO values.trim() to read better content in file

		return values.split(",")
	}

	/**
	 * Click on a random option to replace a product
	 * @param buttonXpath int value for xpath button in DOM
	 * @param itemToReplace String value of product to replace
	 * @param arrayOfValues to compare with
	 */
	@Keyword
	def clickOnRandomReplaceOptionAndVerifyValue(buttonXpath, itemToReplace, arrayOfValues) {

		String replaceButtonXpath = '//*[@id="player-container"]/div[7]/button[' + buttonXpath + ']'

		String replaceOptionXpath = ''

		Random rnd = new Random()

		int randomNumber = rnd.nextInt(3)

		if (randomNumber == 0) { // discard default value 0

			randomNumber = randomNumber + 1
		}

		replaceOptionXpath = '//*[@id="product-list-container"]/ul/li[' + (randomNumber + 1) + ']'

		selectItemIn3Dviewer(itemToReplace) // Fridge "I773788" - Hood "I773776"

		WebUI.delay(2)

		WebUI.waitForElementClickable(WebUI.modifyObjectProperty(findTestObject('WIP/WIP - Sebastien/Dummy-btn'), 'xpath', 'equals', replaceButtonXpath, true), 10)

		WebUI.click(WebUI.modifyObjectProperty(findTestObject('WIP/WIP - Sebastien/Dummy-btn'), 'xpath', 'equals', replaceButtonXpath, true))

		WebUI.click(WebUI.modifyObjectProperty(findTestObject('WIP/WIP - Sebastien/Dummy-btn'), 'xpath', 'equals', replaceOptionXpath , true))

		String replacedProduct = getLastReplacedProduct()

		verifyReplacedProduct(getProductCodeFromRPD(replacedProduct), randomNumber, arrayOfValues)
	}

	/**
	 * Verify a replaced product
	 * @param selectedProduct value of selected style
	 * @param randomNumber int value used to fetch and compare position value in array
	 * @param arrayValuesToCompare array of values used to compare
	 */
	@Keyword
	def verifyReplacedProduct(selectedProduct, randomNumber, arrayValuesToCompare) {

		println(selectedProduct)
		println(randomNumber)
		println(arrayValuesToCompare)

		WebUI.verifyMatch(selectedProduct, arrayValuesToCompare.getAt(randomNumber), true, FailureHandling.CONTINUE_ON_FAILURE)
	}

	/**
	 * Select an over all style
	 * @param xpathValueOfFeature value of wanted feature based on Div number in DOM
	 * @param xpathValueOfOption value of wanted option based on Div number in DOM
	 */
	@Keyword
	def selectOverallStyles(xpathValueOfFeature, xpathValueOfOption) {

		String fullXpathOfFeature = '//*[@id="global-fo"]/div/section[' + xpathValueOfFeature + ']/div/div[' + xpathValueOfOption + ']'

		WebUI.click(WebUI.modifyObjectProperty(findTestObject('WIP/WIP - Sebastien/Dummy-btn'), 'xpath', 'equals', fullXpathOfFeature, true, FailureHandling.CONTINUE_ON_FAILURE))
	}

	/**
	 * Click on a random overall style option
	 * @param featureXpath int value for main button xpath in DOM
	 * @param styleToValidate String feature option name to validate in FOState 
	 */
	@Keyword
	def clickOnRandomStyleOptionAndVerifyValue(featureXpath, styleToValidate, arrayOfValues) {

		String overallStyleFeaturesViewXpath = '//*[@id="global-fo"]/div/section[2]/div/div[' + featureXpath + ']'

		String overallStyleOptionsViewXpath = ''

		Random rnd = new Random()

		int randomNumber = rnd.nextInt(11) // + 2 to compensate for a "not even" DOM structure. (10 for frontAll)

		overallStyleOptionsViewXpath = '//*[@id="global-fo"]/div/section[4]/div/div[' + (randomNumber + 2) + ']'

		WebUI.click(WebUI.modifyObjectProperty(findTestObject('WIP/WIP - Sebastien/Dummy-btn'), 'xpath', 'equals', '//*[@id="style"]', true))

		WebUI.delay(1)

		// Create dynamic object to receive xpath of summary button to validate
		TestObject summaryViewButton = new TestObject("summaryButton")
		String xpath = '//*[@id="styles-top"]/div/button[3]'
		summaryViewButton.addProperty("xpath", ConditionType.EQUALS, xpath)

		if (WebUI.getAttribute(summaryViewButton, 'style') == 'display: inline-block;') { // Summary view is active
			WebUI.click(summaryViewButton) // Change to button main view
		}

		WebUI.delay(1)

		WebUI.takeScreenshot()

		WebUI.click(WebUI.modifyObjectProperty(findTestObject('WIP/WIP - Sebastien/Dummy-btn'), 'xpath', 'equals', overallStyleFeaturesViewXpath, true))

		WebUI.delay(1)

		WebUI.takeScreenshot()

		if (featureXpath == 1) { // Selecting sub section for CounterTop :: TO DO > Validate other categories using sub section

			selectOverallStyles(3, 5) // Selecting SolidWood 40mm
		}

		WebUI.click(WebUI.modifyObjectProperty(findTestObject('WIP/WIP - Sebastien/Dummy-btn'), 'xpath', 'equals', overallStyleOptionsViewXpath , true))

		WebUI.delay(2)

		WebUI.takeScreenshot()

		Map updatedFOState = getCatalogDefaultFOState()

		verifySelectedOverallStyle(updatedFOState.get(styleToValidate), randomNumber, arrayOfValues) // 'Fronts.All' - 'Countertop.Configurable'
	}

	/**
	 * Verify a selected overall style
	 * @param selectedStyle value of selected style
	 * @param randomNumber int value used to fetch and compare position value in array
	 * @param arrayValuesToCompare array of values used to compare
	 */
	@Keyword
	def verifySelectedOverallStyle(selectedStyle, randomNumber, arrayValuesToCompare) {

		WebUI.verifyMatch(selectedStyle, arrayValuesToCompare.getAt(randomNumber), true, FailureHandling.CONTINUE_ON_FAILURE)

	}

	/**
	 * Click on an overall style based on int value from DOM button structure
	 * @param featureXpath int value for main button xpath in DOM
	 * @param optionXpath int value for option button xpath in DOM
	 * @param numberOfOption int value for looping
	 */
	@Keyword
	def String clickOnAllStyleOptions(featureXpath, optionXpath, numberOfOption) {

		String AllOptionValues = ''

		String overallStyleFeaturesViewXpath = '//*[@id="global-fo"]/div/section[2]/div/div[' + featureXpath + ']'

		String overallStyleOptionsViewXpath = ''

		for (def i = 1; i <= numberOfOption; i++ ) {

			overallStyleOptionsViewXpath = '//*[@id="global-fo"]/div/section[4]/div/div[' + (optionXpath + i) + ']'

			WebUI.click(WebUI.modifyObjectProperty(findTestObject('WIP/WIP - Sebastien/Dummy-btn'), 'xpath', 'equals', '//*[@id="style"]', true))

			WebUI.delay(1)

			if (i == 1) {

				WebUI.click(WebUI.modifyObjectProperty(findTestObject('WIP/WIP - Sebastien/Dummy-btn'), 'xpath', 'equals', '//*[@id="styles-top"]/div/button[3]', true))

				WebUI.delay(1)
			}

			WebUI.takeScreenshot()

			WebUI.click(WebUI.modifyObjectProperty(findTestObject('WIP/WIP - Sebastien/Dummy-btn'), 'xpath', 'equals', overallStyleFeaturesViewXpath, true))

			WebUI.delay(1)

			WebUI.takeScreenshot()

			WebUI.click(WebUI.modifyObjectProperty(findTestObject('WIP/WIP - Sebastien/Dummy-btn'), 'xpath', 'equals', overallStyleOptionsViewXpath , true))

			WebUI.delay(1)

			WebUI.takeScreenshot()

			Map updatedFOState = getCatalogDefaultFOState()

			if (i == numberOfOption ) {

				AllOptionValues = AllOptionValues + updatedFOState.get('Fronts.All')

			} else {

				AllOptionValues = AllOptionValues + updatedFOState.get('Fronts.All') + ','
			}
		}

		return AllOptionValues
	}

	@Keyword
	def double getCameraPositionsForSpecifiedAxis(String axis){

		def rpdContent = appEngineApi.getRpdContent(true)
		if(axis.equalsIgnoreCase("x"))
			return rpdContent.ViewingProps.CamFree3D.Pos.x
		else if(axis.equalsIgnoreCase("y"))
			return rpdContent.ViewingProps.CamFree3D.Pos.y
		else if(axis.equalsIgnoreCase("z"))
			return rpdContent.ViewingProps.CamFree3D.Pos.z
		else
			KeywordUtil.markErrorAndStop("Please provide proper axis - X or Y or Z to find position")
	}

	@Keyword
	def double getCameraOriForSpecifiedAxis(String axis){

		def rpdContent = appEngineApi.getRpdContent(true)
		if(axis.equalsIgnoreCase("x"))
			return rpdContent.ViewingProps.CamFree3D.Ori.x
		else if(axis.equalsIgnoreCase("y"))
			return rpdContent.ViewingProps.CamFree3D.Ori.y
		else if(axis.equalsIgnoreCase("z"))
			return rpdContent.ViewingProps.CamFree3D.Ori.z
		else
			KeywordUtil.markErrorAndStop("Please provide proper axis - X or Y or Z to find position")
	}

	@Keyword
	def String getNearestItemIdInSpecificCameraDirection(List<String> expectedKeyPropertyPairList, String axis){

		//ArrayList <String> arr = getItemIDsBasedOnPropertyList(Arrays.asList('Ori: 0 0 0'))
		ArrayList <String> arr = getItemIDsBasedOnPropertyList(expectedKeyPropertyPairList)

		double cameraCoord = getCameraPositionsForSpecifiedAxis(axis)

		HashMap <Double, String> distanceAndItems = new HashMap <Double, String> ()

		for (int i = 0; i < arr.size(); i++ ) {

			//println '--------------'
			String pos = getItemPropertyBasedOnItemId(arr.get(i),'Pos')
			//println (arr.get(i) + " : " + pos.split(" ")[0])
			distanceAndItems.put(Math.abs(cameraCoord - Double.parseDouble(pos.split(" ")[0])), arr.get(i))
		}

		String itemId = distanceAndItems.get(Collections.min(distanceAndItems.keySet()))

		return itemId
	}

	@Keyword
	def ArrayList <String> getItemIdsBasedOnItemTypeAndPropertyList (String itemType, List<String> expectedKeyPropertyPairList){

		RpdProcessing rpdProcessing = new RpdProcessing()

		String all3dItems = rpdProcessing.getItemsJsonAsString()

		println all3dItems

		ArrayList <String> itemList = new ArrayList <String> ()

		def slurper = new groovy.json.JsonSlurper()
		def all3dItemsJsonSlurpedObject = slurper.parseText(all3dItems)

		println all3dItemsJsonSlurpedObject.getClass()

		// ------------ Selection based on item type and property combination

		List <String> actualKeyPropertyPairList = new ArrayList <String> ()

		String expectedItemType = itemType
		String itemId = ''

		for (def object : all3dItemsJsonSlurpedObject) {

			actualKeyPropertyPairList.clear()
			//println object
			//println object.getClass()

			for (entry in object) {

				//println entry.key
				//println entry.value
				//println entry.value.getClass()
				//println entry.value.get('Props')
				//println entry.value.get('Props').getClass()

				if ((entry.value.get('Type').toString()).equals(expectedItemType)) {

					//itemCode = entry.key

					for (propertyEntry in entry.value.get('Props')) {

						String actualKeyPropertyPair = propertyEntry.key + ':' + propertyEntry.value
						actualKeyPropertyPair = actualKeyPropertyPair.trim().replaceAll(" ", "")
						println actualKeyPropertyPair

						actualKeyPropertyPairList.add(actualKeyPropertyPair)
					}

					int propertyMatchCount = 0

					for (String expectedKeyPropertyPair : expectedKeyPropertyPairList) {

						expectedKeyPropertyPair = expectedKeyPropertyPair.trim().replaceAll(" ", "")

						if (actualKeyPropertyPairList.contains(expectedKeyPropertyPair)) {

							propertyMatchCount = propertyMatchCount + 1
						}
					}

					if (propertyMatchCount == expectedKeyPropertyPairList.size()) {

						itemList.add(entry.key)

					}
				}

			}

		}
		return itemList

	}



	/**
	 * Retrieve Item IDs based on partial name of items type and Property List
	 */
	@Keyword
	def ArrayList <String> getItemIDsBasedOnPartialNameOfItemTypesAndPropertyList(String partialNameOfItemType, List<String> expectedKeyPropertyPairList){


		RpdProcessing rpdProcessing = new RpdProcessing()

		String all3dItems = rpdProcessing.getItemsJsonAsString()

		println all3dItems

		ArrayList <String> itemList = new ArrayList <String> ()

		def slurper = new groovy.json.JsonSlurper()
		def all3dItemsJsonSlurpedObject = slurper.parseText(all3dItems)

		println all3dItemsJsonSlurpedObject.getClass()

		// ------------ Selection based on item type and property combination

		List <String> actualKeyPropertyPairList = new ArrayList <String> ()

		String expectedItemType = partialNameOfItemType
		String itemId = ''

		for (def object : all3dItemsJsonSlurpedObject) {

			actualKeyPropertyPairList.clear()

			for (entry in object) {

				if ((entry.value.get('Type').toString()).contains(expectedItemType)) {

					//itemCode = entry.key

					for (propertyEntry in entry.value.get('Props')) {

						String actualKeyPropertyPair = propertyEntry.key + ':' + propertyEntry.value
						actualKeyPropertyPair = actualKeyPropertyPair.trim().replaceAll(" ", "")
						println actualKeyPropertyPair

						actualKeyPropertyPairList.add(actualKeyPropertyPair)
					}

					int propertyMatchCount = 0

					for (String expectedKeyPropertyPair : expectedKeyPropertyPairList) {

						expectedKeyPropertyPair = expectedKeyPropertyPair.trim().replaceAll(" ", "")

						if (actualKeyPropertyPairList.contains(expectedKeyPropertyPair)) {

							propertyMatchCount = propertyMatchCount + 1
						}
					}

					if (propertyMatchCount == expectedKeyPropertyPairList.size()) {

						itemList.add(entry.key)

					}
				}

			}

		}
		return itemList
	}

	/**
	 * Retrieve Item IDs based on Property List
	 */
	@Keyword
	def ArrayList <String> getItemIDsBasedOnPropertyList (List<String> expectedKeyPropertyPairList){

		RpdProcessing rpdProcessing = new RpdProcessing()

		String all3dItems = rpdProcessing.getItemsJsonAsString()

		//println all3dItems

		ArrayList <String> itemList = new ArrayList <String> ()

		def slurper = new groovy.json.JsonSlurper()
		def all3dItemsJsonSlurpedObject = slurper.parseText(all3dItems)

		//println all3dItemsJsonSlurpedObject.getClass()

		// ------------ Selection based on item type and property combination

		List <String> actualKeyPropertyPairList = new ArrayList <String> ()

		String itemId = ''

		for (def object : all3dItemsJsonSlurpedObject) {

			actualKeyPropertyPairList.clear()

			for (entry in object) {

				//itemCode = entry.key

				for (propertyEntry in entry.value.get('Props')) {

					String actualKeyPropertyPair = propertyEntry.key + ':' + propertyEntry.value
					actualKeyPropertyPair = actualKeyPropertyPair.trim().replaceAll(" ", "")
					//println actualKeyPropertyPair

					actualKeyPropertyPairList.add(actualKeyPropertyPair)
				}

				int propertyMatchCount = 0

				for (String expectedKeyPropertyPair : expectedKeyPropertyPairList) {

					expectedKeyPropertyPair = expectedKeyPropertyPair.trim().replaceAll(" ", "")

					if (actualKeyPropertyPairList.contains(expectedKeyPropertyPair)) {

						propertyMatchCount = propertyMatchCount + 1
					}
				}

				if (propertyMatchCount == expectedKeyPropertyPairList.size()) {

					itemList.add(entry.key)
				}
			}

		}
		return itemList
	}

	/**
	 * Retrieve Item IDs based on partial name of items type
	 */
	@Keyword
	def ArrayList <String> getItemIDsBasedOnPartialNameOfItemTypes(String partialNameOfItemType){

		RpdProcessing rpdProcessing = new RpdProcessing()

		String allItems = rpdProcessing.getItemsJsonAsString()

		println allItems

		ArrayList <String> itemList = new ArrayList <String> ()

		def slurper = new groovy.json.JsonSlurper()
		def all3dItemsJsonSlurpedObject = slurper.parseText(allItems)

		println all3dItemsJsonSlurpedObject.getClass()

		// ------------ Selection based on item type, 1st encountered item will be selected

		String expectedItemType = partialNameOfItemType
		String itemCode = ''

		for (def object : all3dItemsJsonSlurpedObject) {

			for (entry in object) {

				if ((entry.value.get('Type').toString()).contains(expectedItemType)) {
					//itemCode = entry.key
					itemList.add(entry.key)
				}
			}

		}

		return itemList
	}

	/**
	 * Update camera position using json file
	 * input - 'JsonBlockPath' filepath
	 */
	@Keyword
	def updateCameraPositionUsingJson(String pathOfJsonToBeFound){

		pathOfJsonToBeFound = RunConfiguration.getProjectDir() + pathOfJsonToBeFound

		JsonReader reader = Json.createReader(new FileReader(pathOfJsonToBeFound))
		JsonObject cameraObject = reader.readObject()

		def rpdObject = new groovy.json.JsonBuilder(appEngineApi.getRpdContent())
		def slurper = new groovy.json.JsonSlurper()
		def rpdSlurpedObject = slurper.parseText(rpdObject.toString())
		def rpdBuilderObject = new groovy.json.JsonBuilder(rpdSlurpedObject)

		def cameraslurpedBlock =  new groovy.json.JsonSlurper().parseText(cameraObject.toString())

		//rpdBuilderObject.content.ViewingProps.CamFree3D=cameraslurpedBlock.ViewingProps.CamFree3D
		rpdBuilderObject.content.ViewingProps=cameraslurpedBlock.ViewingProps

		println(rpdBuilderObject.toPrettyString())

		JavascriptExecutor js = ((DriverFactory.getWebDriver()) as JavascriptExecutor)
		String jsRPDtoLoad = 'var callback = arguments[arguments.length - 1]; AE.provisional.design.load(' + rpdBuilderObject.toString() + ', null, () => {callback(console.log(\"RPD loaded.\"));})'
		js.executeAsyncScript(jsRPDtoLoad)
	}



	/**
	 * Retrieve Product UUID Based On ProductCode And Property
	 */
	@Keyword
	def String getProductIdBasedOnProductCodeAndProperty(String productCode, String propertyCombination) {

		RpdProcessing rpdProcessing = new RpdProcessing()

		String allProducts = rpdProcessing.getProductsJsonAsString()

		println allProducts

		def slurper = new groovy.json.JsonSlurper()
		def allProductsJsonSlurpedObject = slurper.parseText(allProducts)

		println allProductsJsonSlurpedObject.getClass()

		// ------------ Selection based on product code and property combination, 1st encountered item will be selected

		String expectedKeyPropertyPair = propertyCombination
		String expectedProductCode = productCode
		String productId = ''

		for (def object : allProductsJsonSlurpedObject) {

			//println object
			//println object.getClass()

			for (entry in object) {

				//println entry.key
				//println entry.value
				//println entry.value.getClass()
				//println entry.value.get('Props')
				//println entry.value.get('Props').getClass()

				if ((entry.value.get('Code').toString()).equals(expectedProductCode)) {

					productId = entry.key

					for (propertyEntry in entry.value.get('Props')) {

						String actualKeyPropertyPair = propertyEntry.key + ':' + propertyEntry.value
						actualKeyPropertyPair = actualKeyPropertyPair.trim().replaceAll(" ", "")
						println actualKeyPropertyPair

						expectedKeyPropertyPair = expectedKeyPropertyPair.trim().replaceAll(" ", "")

						if (actualKeyPropertyPair.equals(expectedKeyPropertyPair)) {
							productId = entry.key
							break
						}
					}

					if (productId.length() != 0) {
						break
					}
				}

				if (productId.length() != 0) {
					break
				}

			}

			if (productId.length() != 0) {
				break
			}
		}

		if (productId.length() != 0) {
			return productId
		}
		else {
			return null
		}

	}


}


