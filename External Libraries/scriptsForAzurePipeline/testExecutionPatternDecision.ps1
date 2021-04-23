write-host "Changing value"
$today = (get-date).DayOfWeek

$n = (get-date).DayOfWeek.value__
$Result = $n % 2
 
IF ($Result -eq 0)
{
Write-Output ("##vso[task.setvariable variable=startingTestPosition_var;]2")
Write-Output ("##vso[task.setvariable variable=testExecutionOffset_var;]2")
Write-Output ("##vso[task.setvariable variable=patternType;]EVEN Pattern")

}
 
IF ($Result -eq 1)
{
Write-Output ("##vso[task.setvariable variable=startingTestPosition_var;]1")
Write-Output ("##vso[task.setvariable variable=testExecutionOffset_var;]2")
Write-Output ("##vso[task.setvariable variable=patternType;]ODD Pattern")
}
 
IF ($today -eq "Saturday" -or $today -eq "Sunday")
{
Write-Output ("##vso[task.setvariable variable=startingTestPosition_var;]1")
Write-Output ("##vso[task.setvariable variable=testExecutionOffset_var;]1")
Write-Output ("##vso[task.setvariable variable=patternType;]Full run")
}