import Box from '@mui/material/Box'
import {LocalizationProvider, TimeField} from "@mui/x-date-pickers";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import dayjs from "dayjs";

function BasicTimeFields() {
    return (
        <Box
            component="form"
            sx={{
                '& > :not(style)': {m: 1, width: '25ch'},
            }}
            noValidate
            autoComplete="off"
        >
            <LocalizationProvider dateAdapter={AdapterDayjs}>
                <TimeField
                    label="Format with meridiem"
                    defaultValue={dayjs('2022-04-17T15:30')}
                    format="hh:mm a"
                />
                <TimeField
                    label="Format without meridiem"
                    defaultValue={dayjs('2022-04-17T15:30')}
                    format="HH:mm"
                />
                <TimeField
                    label="Format with seconds"
                    defaultValue={dayjs('2022-04-17T15:30')}
                    format="HH:mm:ss"
                />
            </LocalizationProvider>
        </Box>
    )
}

export default BasicTimeFields
