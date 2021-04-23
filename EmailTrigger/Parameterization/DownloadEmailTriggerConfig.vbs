
OutputFilePath = ".\output.txt"
Call DeleteFile(OutputFilePath)

ReadConfigFile

If DeleteProcess = True Then
	Call KillProcess ("Notepad.exe")
	Call KillProcess ("EXCEL.exe")
End If


HTTPDownload URL,DestinationPathToDump 
'HTTPDownload "https://2020spaces-my.sharepoint.com/:x:/p/admin_onedrive_02/EY8Wl20aS5dKtuOD171wIN4BhBvnzv87Drc-retosfOf0w?e=T9eLLc&download=1", "E:\test.xlsx"

''------------------------------------------------

Sub HTTPDownload( myURL, myPath )


    Dim i, objFile, objFSO, objHTTP, strFile, strMsg
    Const ForReading = 1, ForWriting = 2, ForAppending = 8

    ' Create a File System Object
    Set objFSO = CreateObject( "Scripting.FileSystemObject" )

    ' Check if the specified target file or folder exists,
    ' and build the fully qualified path of the target file
    If objFSO.FolderExists( myPath ) Then
        strFile = objFSO.BuildPath( myPath, Mid( myURL, InStrRev( myURL, "/" ) + 1 ) )
    ElseIf objFSO.FolderExists( Left( myPath, InStrRev( myPath, "\" ) - 1 ) ) Then
        strFile = myPath
    Else
        WScript.Echo "ERROR: Target folder not found."
        Exit Sub
    End If

    ' Create or open the target file
    Set objFile = objFSO.OpenTextFile( strFile, ForWriting, True )

    ' Create an HTTP object
    Set objHTTP = CreateObject( "WinHttp.WinHttpRequest.5.1" )

    ' Download the specified URL
    objHTTP.Open "GET", myURL, false
    objHTTP.Send

    ' Write the downloaded byte stream to the target file
    For i = 1 To LenB( objHTTP.ResponseBody )
        objFile.Write Chr( AscB( MidB( objHTTP.ResponseBody, i, 1 ) ) )
    Next

    ' Close the target file
    objFile.Close( )
	
	Call CreateOutputFile 
	
End Sub

''--------------------------------------
Sub CreateOutputFile 
	
	Path = ".\output.txt"
	Dim objFSO,objFile
	Set objFSO = CreateObject( "Scripting.FileSystemObject" )
	
	' If objFSO.FileExists(Path) = True Then	
		' objFSO.DeleteFIle Path
	' End If

    Set	objFile = objFSO.OpenTextFile (Path,2,true)
	objFile.Writeline "DownloadStatusFlag = True"
	objFile.Close
	Set objFSO = Nothing
	
End Sub
		
''--------------------------------------

Sub ReadConfigFile
	
	Path = ".\Config.txt"
	
	Dim fsObj : Set fsObj = CreateObject("Scripting.FileSystemObject")
	Dim vbsFile : Set vbsFile = fsObj.OpenTextFile(Path, 1, False)
	Dim strFileContents : strFileContents = vbsFile.ReadAll
	vbsFile.Close
	Set vbsFile = Nothing
	Set fsObj = Nothing
	ExecuteGlobal strFileContents
	
End Sub

''--------------------------------------

Sub KillProcess (ProcessName)

	Set oShell = CreateObject("Wscript.shell")
		oshell.Run "CMD /C taskkill /f /im "& ProcessName
	Set oShell = Nothing

End Sub
''--------------------------------------

Sub DeleteFile(filePath)

	Path = ".\output.txt"
	Dim objFSO,objFile
	Set objFSO = CreateObject( "Scripting.FileSystemObject" )
	
	If objFSO.FileExists(Path) = True Then	
		objFSO.DeleteFIle Path
	End If
	
	Set objFSO = Nothing
	
End Sub
	
	
	