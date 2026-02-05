import json, urllib.request, urllib.error, sys, time
base='http://localhost:8080'
# Login
login={'username':'admin','password':'admin123'}
req=urllib.request.Request(base+'/api/auth/login', data=json.dumps(login).encode(), headers={'Content-Type':'application/json'})
resp=urllib.request.urlopen(req)
login_res=json.loads(resp.read())
print('Login response:', json.dumps(login_res))
token=login_res['token']
headers={'Content-Type':'application/json','Authorization': 'Bearer '+token}
# Create policy
policy_body={'datasetId':1,'name':'Default Policy','rules':{'columns':[]}}
req2=urllib.request.Request(base+'/api/policies', data=json.dumps(policy_body).encode(), headers=headers, method='POST')
resp2=urllib.request.urlopen(req2)
policy_res=json.loads(resp2.read())
print('Policy created:', json.dumps(policy_res))
policy_id=policy_res.get('id')
# Start job
start_url=f"{base}/api/jobs?datasetId=1&policyId={policy_id}"
req3=urllib.request.Request(start_url, headers=headers, method='POST')
resp3=urllib.request.urlopen(req3)
job_res=json.loads(resp3.read())
print('Job started:', json.dumps(job_res))
