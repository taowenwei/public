import gzip
import base64
import json
from datetime import datetime
import requests
# from botocore.vendored import requests

SAMPLE_EVENT = {
    'awslogs': {
        'data': 'H4sIAAAAAAAAADWQS2/CMBCE/0pkVeJCml2/nRtSUy6tKkFOBVQ5wdBIJEG2Ka0Q/72mj+vMar+ZuZDehWD3rv46OlKSh1k9e3uulsvZvCJTMp4H55MMnCGjjHMlZJIP437ux9MxOYU9h+Jg+2Zri8GFmFvfvv+eLKN3tk83FCgtQBWIxeruaVZXy3pjpbSt00xRprjZOcORmdZZZrYKjNLpRTg1ofXdMXbj8NgdovOBlCvivB99tnXRtTeHbH5g1Ycb4s2/kG6bmEwaybgwmkpONeciRRdMGy4REAEU08IgSgAwKQFFI6RCuHFjlzaJtk/1UIqUTmvJUqjp/1Z/lXJQOWINUFIsgd1LA6/raJigTaN4LsBizncU8wZ0mxsKqduuEZzLdawWi5fFOl6yBIqnUGYCYJr1YV9mk5jomfts3U/zSXZdD+S6uX4DmLQ+pKsBAAA='
    }
}
WEBHOOK_URL = 'https://lucidmotors.webhook.office.com/webhookb2/ec3913f9-eb33-4eb8-88f8-dca59c23516b@da8905ac-f28e-4758-a154-2edf347f9216/IncomingWebhook/987fae6b41b3479d83f82aa99cd8af12/2fb7b970-71da-4680-8a19-424646502914'


def event2json(event):
    base64encoded = event['awslogs']['data']
    gzBinary = base64.b64decode(base64encoded)
    jsonString = gzip.decompress(gzBinary)
    logEntry = json.loads(jsonString)
    print(json.dumps(logEntry, indent=2))
    return logEntry


def prepareWebHookMsg(logJson):
    logGroup = logJson['logGroup']
    logStream = logJson['logStream']
    logFilterName = logJson['subscriptionFilters']
    id = logJson['logEvents'][0]['id']
    timestamp = logJson['logEvents'][0]['timestamp']
    time = datetime.fromtimestamp(timestamp/1000)
    errorMsg = logJson['logEvents'][0]['message']

    template = {
        '@type': 'MessageCard',
        '@context': 'http://schema.org/extensions',
        'themeColor': '0076D7',
        'summary': f'{logFilterName} created a new task',
        'sections': [{
            'activityTitle': logStream,
            'activitySubtitle': logGroup,
            'activityImage': 'https://teamsnodesample.azurewebsites.net/static/img/image5.png',
            'facts': [{
                'name': 'id',
                'value': id
            }, {
                'name': 'message',
                'value': errorMsg
            }, {
                'name': 'timestamp',
                'value': timestamp.isoformat()
            }],
            'markdown': True
        }]
    }
    print(json.dumps(template, indent=2))
    return template


def lambda_handler(event, context):
    logjson = event2json(event)
    msgCard = prepareWebHookMsg(logjson)
    requests.post(WEBHOOK_URL, json=msgCard)
    return {
        'statusCode': 200
    }


lambda_handler(SAMPLE_EVENT, None)
