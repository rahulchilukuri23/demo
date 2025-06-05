{{- define "ev-management.name" -}}
{{ .Chart.Name }}
{{- end }}

{{- define "ev-management.fullname" -}}
{{ .Release.Name }}-{{ .Chart.Name }}
{{- end }}
