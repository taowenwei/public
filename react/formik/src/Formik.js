import * as React from "react";
import { useFormik } from "formik";
import country from "country-list-js";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Stack from "@mui/material/Stack";
import Autocomplete from "@mui/material/Autocomplete";
import CircularProgress from "@mui/material/CircularProgress";

const getStates = () => {
  const us = country.findByName("United States");
  return us.provinces.map((p) => p.name);
};

const InputSpecs = [
  {
    name: "firstName",
    label: "First Name",
    type: "text",
    validator: (value) => {
      if (!value) return "Required";
      if (value.length > 10) return "Must be 10 characters or less";
      return undefined;
    },
  },
  {
    name: "lastName",
    label: "Last Name",
    type: "text",
    validator: (value) => {
      if (!value) return "Required";
      if (value.length > 15) return "Must be 15 characters or less";
      return undefined;
    },
  },
  {
    name: "state",
    label: "State",
    type: "autoComplete",
    validator: (value) => {
      if (!value) return "Required";
      return undefined;
    },
  },
  {
    name: "email",
    label: "Email",
    type: "text",
    validator: (value) => {
      if (!value) return "Required";
      if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(value)) {
        return "Invalid email address";
      }
      return undefined;
    },
  },
];

const AsyncAutoComplete = (props) => {
  const { formik, name, label } = props;
  const [open, setOpen] = React.useState(false);
  const [options, setOptions] = React.useState([]);
  const loading = open && options.length === 0;
  const sleep = (duration) =>
    new Promise((resolve) => {
      setTimeout(() => {
        resolve();
      }, duration);
    });

  React.useEffect(() => {
    let active = true;
    if (!loading) {
      return undefined;
    }
    (async () => {
      await sleep(1e3); // For demo purposes.
      if (active) {
        setOptions(getStates());
      }
    })();
    return () => {
      active = false;
    };
  }, [loading]);

  React.useEffect(() => {
    if (!open) {
      setOptions([]);
    }
  }, [open]);

  return (
    <Autocomplete
      id={name}
      size="small"
      sx={{ width: 300 }}
      open={open}
      onOpen={() => {
        setOpen(true);
      }}
      onClose={() => {
        setOpen(false);
      }}
      isOptionEqualToValue={(option, value) => option === value}
      getOptionLabel={(option) => option}
      options={options}
      loading={loading}
      renderInput={(params) => (
        <TextField
          {...params}
          label={label}
          InputProps={{
            ...params.InputProps,
            endAdornment: (
              <React.Fragment>
                {loading ? (
                  <CircularProgress color="inherit" size={20} />
                ) : null}
                {params.InputProps.endAdornment}
              </React.Fragment>
            ),
          }}
          error={formik.touched[name] && Boolean(formik.errors[name])}
          helperText={formik.touched[name] && formik.errors[name]}
        />
      )}
      value={formik.values[name]}
      onChange={(_, value) => formik.setFieldValue(name, value ?? "")}
    />
  );
};

const MuiFactory = (formik, spec, key) => {
  switch (spec.type) {
    case "text":
      return (
        <TextField
          fullWidth
          size="small"
          id={spec.name}
          name={spec.name}
          label={spec.label}
          key={key}
          value={formik.values[spec.name]}
          onChange={formik.handleChange}
          onBlur={formik.handleBlur}
          error={formik.touched[spec.name] && Boolean(formik.errors[spec.name])}
          helperText={formik.touched[spec.name] && formik.errors[spec.name]}
        />
      );
    case "autoComplete":
      return (
        <AsyncAutoComplete
          name={spec.name}
          label={spec.label}
          formik={formik}
          key={key}
        />
      );
    default:
      return undefined;
  }
};

export const SignupForm = () => {
  const formik = useFormik({
    initialValues: InputSpecs.reduce((accum, curr) => {
      switch (curr.type) {
        case "text":
          accum[curr.name] = "";
          break;
        default:
          accum[curr.name] = null;
      }
      return accum;
    }, {}),
    validate: (values) => {
      const errors = Object.keys(values).reduce((accum, key) => {
        const spec = InputSpecs.find((spec) => spec.name === key);
        if (!!spec) {
          const error = spec.validator?.(values[key]);
          if (!!error) {
            accum[key] = error;
          }
        }
        return accum;
      }, {});
      return errors;
    },
    onSubmit: (values) => {
      alert(JSON.stringify(values, null, 2));
    },
  });
  return (
    <Stack spacing={2} direction="column">
      {InputSpecs.map((spec, index) => MuiFactory(formik, spec, index))}

      <Button
        color="primary"
        variant="contained"
        type="submit"
        onClick={formik.handleSubmit}
      >
        Submit
      </Button>
    </Stack>
  );
};
