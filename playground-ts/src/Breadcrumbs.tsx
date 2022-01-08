import {SyntheticEvent} from 'react'
import Typography from '@mui/material/Typography'
import Breadcrumbs from '@mui/material/Breadcrumbs'
import Link from '@mui/material/Link'

function handleClick (event: SyntheticEvent) {
  event.preventDefault()
  console.info('You clicked a breadcrumb.')
}

function BasicBreadcrumbs () {
  return (
    <div role="presentation" onClick={handleClick}>
      <Breadcrumbs aria-label="breadcrumb">
        <Link underline="hover" color="inherit" href="https://mui.com/">
          MUI
        </Link>
        <Link
          underline="hover"
          color="inherit"
          href="https://mui.com/getting-started/installation/"
        >
          Core
        </Link>
        <Typography color="text.primary">Breadcrumbs Primary</Typography>
        <Typography color="text.secondary">Breadcrumbs Secondary</Typography>
      </Breadcrumbs>
    </div>
  )
}

export default BasicBreadcrumbs
