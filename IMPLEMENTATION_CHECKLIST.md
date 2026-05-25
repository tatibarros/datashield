# DataShield – Implementation Checklist

## Phase 1 – MVP Core
- [ ] JWT Authentication and RBAC (Admin/Analyst/Auditor)
- [ ] Dataset upload (CSV/JSON), listing, preview (50 rows)
- [ ] PII detection (heuristics)
- [ ] Policy creation (mask, hash, suppress, generalize)
- [ ] Asynchronous processing via jobs
- [ ] Basic audit log
- [ ] Result download/export

## Phase 2 – Expansion
- [ ] Policy versioning and editing
- [ ] Improved PII detection (regex, pattern)
- [ ] More anonymization algorithms (perturbation, k-anonymity)
- [ ] Data profiling visualizations
- [ ] Batch job scheduling
- [ ] User management UI for admins
- [ ] Data retention/cleanup

## Phase 3 – Polish & Advanced
- [ ] Integration tests (backend & frontend)
- [ ] Performance improvements for large data
- [ ] Internationalization (multi-language)
- [ ] UI/UX polish, error messages
- [ ] Advanced audit log filtering/exports

## Testing
- [ ] Backend: `cd backend && mvn test`
- [ ] Frontend: `cd frontend && npm test`
- [ ] API tested via Swagger/Postman

## Notes for Lovable
- Prioritize stability and RESTful patterns
- Tests for anonymization logic (Mask, Hash)
- Use provided demo creds
- See `ARCHITECTURE.md` for reference flows

---
Last updated: 2026-05-25