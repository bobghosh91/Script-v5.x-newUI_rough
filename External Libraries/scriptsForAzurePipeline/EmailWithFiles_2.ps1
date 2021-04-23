#This script sends email with multiple file attachments. 

Param (
[string]$workspace = " ",
[string]$Subject = ' ',
[string[]]$Email = "",
[string]$Body = "",
[string]$statFilePath = ""
) 

#$filepath = "D:\AutomationStatReportDemo.xlsx"
$filepath = $statFilePath
$sheetName = 1
$listOfColumnsToPrint=@("ID","REPORT NAME","PASSED","FAILED","VERIFY","NOT EXECUTED","BLOCKED","Mahesh","Sayali","Vedprakash","Vahid","Tanmay")

      
function readFile(){
if ((Test-Path $filepath) -eq $TRUE){
#Create an instance of Excel.Application and Open Excel file 
$objExcel = new-object -comobject excel.application  
$objExcel.Visible = $false  
$objWorkbook = $objExcel.Workbooks.Open($filepath) 
$Worksheet = $objWorkbook.Worksheets.Item($sheetName)
 
$rowMax = ($Worksheet.UsedRange.Rows).count
$colMax = ($Worksheet.UsedRange.Columns).count

#$indexArray = New-Object System.Collections.ArrayList
$indexList = New-Object System.Collections.Generic.List[System.Object]

foreach ($col in $listOfColumnsToPrint) {

    $Found = $Worksheet.Cells.Find($col,[Type]::Missing,[Type]::Missing,1) # ([Type]::Missing,[Type]::Missing,1) It  will return the first cell which match the exact text in search string.
    
    $indexList.Add($Found.Column)
    
}

for ($i=1; $i -le $rowMax; $i++){
    
    $data=$null

    foreach ($colindex in $indexList) {
   
    $cellData =$worksheet.Rows.Item($i).Columns.Item($colindex).Text
    
    if ($i-eq 1){
        $dataRow = "<th>$cellData</th>"
         } else{
            $dataRow = "<td>$cellData</td>"
         }
        $data+=$datarow
     }
     
    $tabledata+="<tr>$data</tr>"
     
  }
  $objExcel.quit()
 
return $tabledata
}
}

$HTMLtabledata=readFile

if ((Test-Path $filepath) -eq $TRUE ){
$getHTMLFromData = "
    <html>
    <style>
    {font-family: Arial; font-size: 13pt;}
    TABLE{border: 1px solid black; border-collapse: collapse; font-size:13pt;}
    TH{border: 1px solid black; background: #dddddd; padding: 5px; color: #000000;}
    TR{border: 1px solid black; padding: 5px; }
    TD{border: 1px solid black; padding: 5px;text-align: center }
    </style>
    <h3>Statistics :</h3>
    <table>
    $HTMLtabledata
    </table>
    </html><br>
    <br>Thanks and Regards,<br>
    Automation Team
   "
   }else{

   $message="File Not Found"
   $getHTMLFromData = "
    <html>
    <h3>Email Report</h3>
   <span style = 'color:red;font-size:13pt;'> $message</span><br><br>
    </html>
     <br>Thanks and Regards,<br>
     Automation Team
   "
   }


$body1 = $Body.replace("C:\","")
[array]$attachments = Get-ChildItem "$workspace\External Libraries\reportsFolder" *.*
#[array]$attachments = Get-ChildItem "$workspace" *.txt
$Msg = @{
    to          = $Email
    from        = "qaguiautomationtest1@2020spaces.com"
    Body        = $body1 + $getHTMLFromData
    subject     = "$Subject"
    smtpserver  = "mail.2020.net"
    BodyAsHtml  = $True
    Attachments = $attachments.fullname
}
Send-MailMessage @Msg