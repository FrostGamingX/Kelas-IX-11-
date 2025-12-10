# Enable Excel Import for Classes

## Tasks
- [ ] Modify app/Utils/SpreadsheetReader.php to use box/spout for XLSX parsing
- [ ] Update parseXlsx method to use Box\Spout\Reader\Common\Creator\ReaderFactory
- [ ] Ensure output format matches current CSV parser (associative arrays with normalized headers)
- [ ] Test XLSX import functionality

## Status
- Plan approved by user
- Ready to implement changes

# Fixed: Student Page Crash on Schedule Loading

## Issue
- Application crashed when opening student page and loading schedule data
- Root cause: schedules.guru_id referenced user IDs but foreign key changed to reference teachers table
- Teacher records have different IDs than original user IDs

## Solution
- Created migration to update guru_id in schedules table to point to correct teacher IDs
- Migration matches teachers by email to ensure correct mapping

## Status
- [x] Identified root cause
- [x] Created migration to fix data
- [x] Migration executed successfully
- [x] Issue resolved
