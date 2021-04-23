
Write-Output $(testSuiteDivision)

IF ("$(testSuiteDivision)" -eq "NO")
{
Write-Output ("##vso[task.setvariable variable=testSuiteCollectionPath1;]Test Suites/00_Collections/Metric_Parallel")
Write-Output $(testSuiteCollectionPath1)
}
IF ("$(testSuiteDivision)" -eq "YES")
{
cd '$(build.sourcesDirectory)\External Libraries'

java -jar  testSuiteDistributor.jar testSuiteDistributor.TestSuiteDistributor "../Test Suites/00_Collections/Metric_Parallel.ts" "$(parallelSession)" "$(testsPerSuite)"

Write-Output ("##vso[task.setvariable variable=testSuiteCollectionPath1;]Test Suites/00_Collections/Metric_Parallel_temp")

Write-Output $(testSuiteCollectionPath1)
}
