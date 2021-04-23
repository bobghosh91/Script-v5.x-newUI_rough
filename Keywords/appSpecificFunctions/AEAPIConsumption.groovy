package appSpecificFunctions

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.openqa.selenium.JavascriptExecutor

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

public class AEAPIConsumption {

	@Keyword
	def Object getRpdContent(boolean asyncJsExecution = false) {

		//Below string represents the selenium syntax in async javascript, to obtain RPD content as JSON
		String rpdJsStmt = "var callback = arguments[arguments.length - 1]; callback(AE.provisional.design.serialize({toJSON: true}));"
		
		JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()
		def rpdResponse = js.executeAsyncScript(rpdJsStmt)
		return rpdResponse
	}

	@Keyword
	def Object setCatalogCodeAndVersionInGlobalVariables() {

		try {

			JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()

			//Below string represents the selenium syntax in async javascript, to obtain Default Catalog Id as JSON
			String getDefaultCatalogIdJsStmt = "var callback = arguments[arguments.length - 1]; AE.catalog.getDefaultCatalogs({},function(e,d){callback(d);});"
			def defaultCatalogIdResponse = js.executeAsyncScript(getDefaultCatalogIdJsStmt)
			//println (defaultCatalogIdResponse[0].id)

			//Below string represents the selenium syntax in async javascript, to obtain Default Catalog details using Id, as JSON
			String getDefaultCatalogDetailsJsStmt = "var callback = arguments[arguments.length - 1]; AE.catalog.getCatalogInfo(\"" + defaultCatalogIdResponse[0].id + "\",function(e,d){callback(d);});"
			def defaultCatalogDetailsResponse = js.executeAsyncScript(getDefaultCatalogDetailsJsStmt)
			//println (defaultCatalogDetailsResponse.version)
			//println (defaultCatalogDetailsResponse.refCode.internal)

			GlobalVariable.envInfo.put("Catalog Details", "[" + defaultCatalogDetailsResponse.refCode.internal + ": " + defaultCatalogDetailsResponse.version + "]")
		}
		catch (Exception e) {

			GlobalVariable.envInfo.put("Catalog Details", "Couldn't retrieve (Site Load Issue/Browser Terminated/Unknown Issue)")
		}
	}

	@Keyword
	def Object setAppDetailsInGlobalVariables() {

		try {
			JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()

			String isCoreName = (String) js.executeScript("return (require('json!config/environment.json').appName)")
			String isCoreVersion = (String) js.executeScript("return (require('json!config/environment.json').appVersion)")
			String appName = (String) js.executeScript("return (require('config').appName)")
			String appVersion = (String) js.executeScript("return (require('config').appVersion)")

			GlobalVariable.envInfo.put("App Details", ("[" + isCoreName + ": " + isCoreVersion + "] [" + appName + ": " + appVersion + "]"))
		}
		catch (Exception e) {

			GlobalVariable.envInfo.put("App Details", "Couldn't retrieve (Site Load Issue/Browser Terminated/Unknown Issue)")
		}
	}

	@Keyword
	def unselectAllItems() {

		//Below string represents the selenium syntax in sync javascript, to obtain RPD content as JSON
		String itemSelectionSynchronousJsStmt = 'AE.provisional.design.unselectAll();'

		JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()
		js.executeScript(itemSelectionSynchronousJsStmt)
	}

	@Keyword
	def moveProductToTargetPosition(String productID, String targetX, String targetY, String targetZ) {

		//Below string represents the selenium syntax in sync javascript, to move the Product to target Position
		String productMovementynchronousJsStmt = 'AE.provisional.products.move("' + productID + '", {position:{x: ' + targetX + ' , y: ' + targetY + ' ,z: ' + targetZ + '}}, function(a){console.log(a)});'

		println productMovementynchronousJsStmt

		JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()
		js.executeScript(productMovementynchronousJsStmt)

	}

	@Keyword
	def selectItemBasedOnItemId(String itemId) {

		//Below string represents the selenium syntax in sync javascript, to obtain RPD content as JSON
		String itemSelectionSynchronousJsStmt = 'AE.provisional.design.selectItems([\"' + itemId + '\"]);'

		JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()

		js.executeScript(itemSelectionSynchronousJsStmt)

		WebUI.delay(2)

		//Workaround for defect 366415 (context menu not visible on selecting item first time), only for specific test case list
		if (GlobalVariable.currentTestCaseID in ['347545','339227','337778', '234247', '352456', '249811', '249814', '234286', '234297', '234314', '234235', '234272', '236493', '236491', '234312', '234388', '236496', '236495', '352467', '337030', '348112', '354431', '337032', '350798', '350800', '234248', '234370', '341112', '347537', '347543', '347544']) {

			String itemSelectionSynchronousJsStmt2 = 'AE.provisional.design.unselectAll();'

			js.executeScript(itemSelectionSynchronousJsStmt2)

			WebUI.delay(2)

			js.executeScript(itemSelectionSynchronousJsStmt)

			WebUI.delay(2)
		}
	}

	@Keyword
	def Object getCameraType() {

		//Below string represents the selenium syntax in async javascript, to obtain camera type
		String getCameraSynchronousJsStmt = 'var callback = arguments[arguments.length - 1];' +
				'var camMgr = _AE.GetModule("DesignMgr").GetControlDispatcher().Get3DControl().getCameraMgr();' +
				'callback(camMgr.GetCurrentCameraType());' // "Free3D", "Front2D", "Top2D" etc.

		JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()
		def cameraType = js.executeAsyncScript(getCameraSynchronousJsStmt)
		return cameraType
	}

	@Keyword
	def forceCameraPosAndOri(double xPos, double yPos, double zPos, double xOriInRadian, double yOriInRadian, double zOriInRadian) {

		//Below string represents the selenium syntax in async javascript, to force camera pos and ori

		JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()

		//js.executeScript('camera = WASMPlayerModule.GetEngine3D().GetActiveCamera();')
		//js.executeScript('camera.GetKS().SetPosXYZ(' + xPos + ',' + yPos + ',' + zPos + ');')
		//js.executeScript('camera.GetKS().SetOriXYZ(' + xOriInRadian + ',' + yOriInRadian + ',' + zOriInRadian + ');')

		js.executeScript('camera = WASMPlayerModule.GetEngine3D().GetActiveCamera();' +
				'camera.GetKS().SetPosXYZ(' + xPos + ',' + yPos + ',' + zPos + ');' +
				'camera.GetKS().SetOriXYZ(' + xOriInRadian + ',' + yOriInRadian + ',' + zOriInRadian + ');')

		//println "out"
	}

	@Keyword
	def Object twoD_getPlacementZonePos(String itemId, String pointLocation="Center") {

		//Below string represents the selenium syntax in async javascript, to obtain cabinet zone on-screen location
		String getItemLocationAsynchronousJsStmt = 'var callback = arguments[arguments.length - 1];' +
				'var control = _AE.GetModule("DesignMgr").GetControlDispatcher().ListControlUUIDs()[0];' +
				'var v2d = _AE.GetModule("DesignMgr").GetControlDispatcher().GetControl(control);' +
				'callback(v2d.GetPlacementZoneCanvasPos(\"'+ itemId +'\", \"' + pointLocation + '\"));'

		JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()
		def itemLocationPoint = js.executeAsyncScript(getItemLocationAsynchronousJsStmt)
		return itemLocationPoint
	}

	@Keyword
	def Object twoD_getApplianceBlockPos(String itemType) {

		//Below string represents the selenium syntax in async javascript, to obtain appliance block on-screen location, on Appliance Location page
		String getItemLocationAsynchronousJsStmt = 'var callback = arguments[arguments.length - 1];' +
				'var control = _AE.GetModule("DesignMgr").GetControlDispatcher().ListControlUUIDs()[0];' +
				'var v2d = _AE.GetModule("DesignMgr").GetControlDispatcher().GetControl(control);' +
				'callback(v2d.GetApplianceCanvasPos(\"'+ itemType + '\"));'

		JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()
		def itemLocationPoint = js.executeAsyncScript(getItemLocationAsynchronousJsStmt)
		return itemLocationPoint
	}

	@Keyword
	def Object twoD_getItemPos(String itemId) {

		//Below string represents the selenium syntax in async javascript, to obtain on-screen location of an item
		String getItemLocationAsynchronousJsStmt = 'var callback = arguments[arguments.length - 1];' +
				'var control = _AE.GetModule("DesignMgr").GetControlDispatcher().ListControlUUIDs()[0];' +
				'var v2d = _AE.GetModule("DesignMgr").GetControlDispatcher().GetControl(control);' +
				'callback(v2d.GetCanvasPosOfItem(\"'+ itemId +'\"));'

		JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()
		def itemLocationPoint = js.executeAsyncScript(getItemLocationAsynchronousJsStmt)
		return itemLocationPoint
	}

	@Keyword
	def Object twoD_getItemPosAndRect(String itemId) {
		//	var v2d = dispatcher.GetFirstControlOfType("2DControl");

		//Below string represents the selenium syntax in async javascript, to obtain on-screen location and rectangle of an item
		String getItemLocationAsynchronousJsStmt = 'var callback = arguments[arguments.length - 1];' +
				'var control = _AE.GetModule("DesignMgr").GetControlDispatcher().ListControlUUIDs()[0];' +
				'var v2d = _AE.GetModule("DesignMgr").GetControlDispatcher().GetFirstControlOfType("2DControl");' +
				'callback(v2d.GetObjectCanvasPosAndRect(\"'+ itemId +'\"));'

		JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()
		def itemLocation = js.executeAsyncScript(getItemLocationAsynchronousJsStmt)
		return itemLocation
	}

	@Keyword
	def Object twoD_getItemDraggerPosAndRect(String itemId, String whichSide) {

		//Below string represents the selenium syntax in async javascript, to obtain on-screen location and rectangle of an item draggers
		//Note:
		//1. This API is not useful for obtaining wall endpoints
		//2. "whichSide" can be “left”, “right”, “back” or “front”

		String getItemLocationAsynchronousJsStmt = 'var callback = arguments[arguments.length - 1];' +
				'var control = _AE.GetModule("DesignMgr").GetControlDispatcher().ListControlUUIDs()[0];' +
				'var v2d = _AE.GetModule("DesignMgr").GetControlDispatcher().GetFirstControlOfType("2DControl");' +
				'callback(v2d.GetDraggerCanvasPosAndRect(\"'+ itemId +'\", \"' + whichSide + '\"));'

		JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()
		def itemLocation = js.executeAsyncScript(getItemLocationAsynchronousJsStmt)
		return itemLocation
	}

	@Keyword
	def Object twoD_getWallCornerPointPos(String itemId, String isItleftCorner) {

		//Below string represents the selenium syntax in async javascript, to obtain on-screen location of wall end point
		String getItemEndpointAsynchronousJsStmt = 'var callback = arguments[arguments.length - 1];' +
				'var control = _AE.GetModule("DesignMgr").GetControlDispatcher().ListControlUUIDs()[0];' +
				'var vPlayer = _AE.GetModule("DesignMgr").GetControlDispatcher().GetControl(control);' +
				'callback(vPlayer.GetCornerPointCanvasPos(\"'+ itemId +'\", ' + isItleftCorner + '));'

		JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()
		def itemLocationPoint = js.executeAsyncScript(getItemEndpointAsynchronousJsStmt)
		return itemLocationPoint
	}

	@Keyword
	def Object threeD_selectItemAndGetPosBasedOnItemId(String itemId) {

		//Below string represents the selenium syntax in async javascript, to obtain on-screen location item in 3D view
		String itemSelectionAsynchrhousJsStmt = 'var callback = arguments[arguments.length - 1];' +
				'AE.provisional.design.selectItems([\'' + itemId + '\'], () => {' +
				'AE.provisional.design.getSelectedItems(null, (i_aItems) => {' +
				'var l_aPixXYPayload = [i_aItems.map(i_oItem => Object({Item: i_oItem, Placement: \'middle\' }))];' +
				'AE.provisional.events.register(\"OnPixXYReceived\", function(i_aPixXYList) {' +
				'callback(i_aPixXYList[0].ScreenPos);' +
				'AE.provisional.events.unregister(\"OnPixXYReceived\");' +
				'});' +
				'AE.provisional.events.notify(\'RetrievePixXY\', l_aPixXYPayload);' +
				'});' +
				'});'

		JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()
		def response = js.executeAsyncScript(itemSelectionAsynchrhousJsStmt)
		println response
		return response
	}

	@Keyword
	def Object player_getItemPosBasedOnItemId(String itemId) {

		//Below string represents the selenium syntax in async javascript, to obtain on-screen location item in 3D view
		String itemSelectionAsynchrhousJsStmt =
				'var callback = arguments[arguments.length - 1];' +
				'uuid="' + itemId + '";' +
				'MOID = Number(_AE.GetModule("DesignMgr").GetDesignList()[0].GetControlDispatcher().Get3DControl().getItemCache()[uuid].itemRef.GetMOID());' +
				'my3DObj= Module.GetEngine3D().GetScene().Get3DObjectByMOID(MOID);' +
				'callback(screenPosXY = Module.GetEngine3D().GetScreenCoordinateForActiveViewerFromObject3D(my3DObj));'
		//'console.log(\'x=\'+screenPosXY.x+\' y=\'+screenPosXY.y);'

		JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()
		def response = js.executeAsyncScript(itemSelectionAsynchrhousJsStmt)
		println response
		return response
	}

	@Keyword
	def Object threeD_selectItemAndGetManipulatorPosBasedOnItemId(String itemId, String arrowType) {

		//Below string represents the selenium syntax in sync javascript, to obtain on-screen location item in arrow manipulators

		String itemSelectionAsynchrhousJsStmt =
				'arrow = Module.GetEngine3D().FindFirstObjectByName(Module.GetEngine3D().GetScene(), "' + arrowType + '");' +
				'if (arrow.IsVisible()) { console.log(arrow.GetName() + " visible"); }' +
				'return(Module.GetEngine3D().GetScreenCoordinateForActiveViewerFromObject3D(arrow));'

		JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()
		def response = js.executeScript(itemSelectionAsynchrhousJsStmt)
		println response
		return response
	}


	//Supported Arrow types : Arrow_E, Arrow_W, Arrow_S, Arrow_N, Arrow_Up, Arrow_Down, RotatorTipExtrusion270b, RotatorTipExtrusion90b
	@Keyword
	def Object player_selectItemAndGetManipulatorPosBasedOnItemId(String itemId, String arrowType) {

		//Below string represents the selenium syntax in sync javascript, to obtain on-screen location item in arrow manipulators

		String itemSelectionAsynchrhousJsStmt =
				'arrow = WASMPlayerModule.GetEngine3D().FindFirstObjectByName(WASMPlayerModule.GetEngine3D().GetScene(), "' + arrowType + '");' +
				'if (arrow.IsVisible()) { console.log(arrow.GetName() + " visible"); }' +
				'return(WASMPlayerModule.GetEngine3D().GetScreenCoordinateForActiveViewerFromObject3D(arrow));'

		JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()
		def response = js.executeScript(itemSelectionAsynchrhousJsStmt)
		println response
		return response
	}

	@Keyword
	def loadRPDfromFile(String pathOfRPDToBeFound) {

		JavascriptExecutor js = ((DriverFactory.getWebDriver()) as JavascriptExecutor)

		JsonReader reader = Json.createReader(new FileReader(RunConfiguration.getProjectDir() + pathOfRPDToBeFound))

		JsonObject RPDfromFile = reader.readObject()

		String jsRPDtoLoad = 'var callback = arguments[arguments.length - 1]; AE.provisional.design.load(' + RPDfromFile.toString() + ', null, () => {callback(console.log(\"RPD loaded.\"));})'

		js.executeAsyncScript(jsRPDtoLoad)
	}

	@Keyword
	def boolean isItemInAnimatedState(String itemId) {

		JavascriptExecutor js = ((DriverFactory.getWebDriver()) as JavascriptExecutor)

		String jsStmt = "var callback = arguments[arguments.length - 1];" +
				"AE.provisional.design.isItemAnimated('" + itemId + "', function(a){console.log(callback(a))})"

		def response = js.executeAsyncScript(jsStmt)

		return response
	}
}
